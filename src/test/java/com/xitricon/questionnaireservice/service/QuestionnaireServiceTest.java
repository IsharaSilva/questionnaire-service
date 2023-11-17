package com.xitricon.questionnaireservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xitricon.questionnaireservice.common.exception.ResourceNotFoundException;
import com.xitricon.questionnaireservice.dto.QuestionOutputDTO;
import com.xitricon.questionnaireservice.dto.QuestionServiceOutputDTO;
import com.xitricon.questionnaireservice.dto.QuestionnaireOutputDTO;
import com.xitricon.questionnaireservice.model.Question;
import com.xitricon.questionnaireservice.model.Questionnaire;
import com.xitricon.questionnaireservice.model.QuestionnairePage;
import com.xitricon.questionnaireservice.model.enums.QuestionType;
import com.xitricon.questionnaireservice.repository.QuestionnaireRepository;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

class QuestionnaireServiceTest {

	private QuestionnaireService questionnaireService;

	@Mock
	QuestionnaireRepository questionnaireRepository;

	private final String findQuestionUrl = "http://localhost:8081/api/questions/";

	private MockRestServiceServer mockServer;
	private final ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		RestTemplate restTemplate = new RestTemplate();
		mockServer = MockRestServiceServer.createServer(restTemplate);
		questionnaireService = new QuestionnaireServiceImpl(questionnaireRepository, restTemplate, findQuestionUrl);
	}

	@AfterEach
	public void cleanUp() {
		this.questionnaireRepository.deleteAll();
	}
	@Test
	public void testAddQuestionToQuestionnaire() throws URISyntaxException, JsonProcessingException {
		String questionnaireId = "654c0048d7db1379df7e7e7e";
		String questionId = "654c0048d7db1379df7e7f1e";
		String title = "Single Answer type Question";
		QuestionType questionType = QuestionType.SINGLE_ANSWER;

		Questionnaire questionnaireToBeSaved = getQuestionnaire(title, questionType, questionnaireId);
		assertNotNull(questionnaireToBeSaved);
		doReturn(questionnaireToBeSaved).when(questionnaireRepository).save(any());

		QuestionServiceOutputDTO expectedQuestionOutput = new QuestionServiceOutputDTO(questionId, title, questionType,
				new ArrayList<>());

		mockServer.expect(ExpectedCount.once(), requestTo(new URI(findQuestionUrl + questionId)))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatusCode.valueOf(HttpStatus.SC_OK)).contentType(MediaType.APPLICATION_JSON)
						.body(mapper.writeValueAsString(expectedQuestionOutput)));

		QuestionnaireOutputDTO questionnaireOutputDTO = questionnaireService.addQuestionToQuestionnaire(
				questionnaireToBeSaved.getId(), questionId,
				questionnaireToBeSaved.getPages().get(0).getId().toString());

		QuestionOutputDTO savedQuestion = questionnaireOutputDTO.getPages().get(0).getQuestions().stream()
				.filter(questionOutputDTO -> questionOutputDTO.getId().equals(questionId)).findFirst().orElse(null);

		assertThat(savedQuestion, is(notNullValue()));
		assertThat(savedQuestion.getId(), is(questionId));
		assertThat(savedQuestion.getLabel(), is(title));
		assertThat(savedQuestion.getType(), is(questionType.toString()));
	}

	@Test
	public void testAddQuestionWithInvalidPageId() throws URISyntaxException, JsonProcessingException {
		String questionnaireId = "654c0048d7db1379df7e7e7e";
		String questionId = "654c0048d7db1379df7e7f1e";
		String title = "Single Answer type Question";
		QuestionType questionType = QuestionType.SINGLE_ANSWER;

		Questionnaire questionnaireToBeSaved = getQuestionnaire(title, questionType, questionnaireId);

		QuestionServiceOutputDTO expectedQuestionOutput = new QuestionServiceOutputDTO(questionId, title, questionType,
				new ArrayList<>());

		mockServer.expect(ExpectedCount.once(), requestTo(new URI(findQuestionUrl + questionId)))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatusCode.valueOf(HttpStatus.SC_OK)).contentType(MediaType.APPLICATION_JSON)
						.body(mapper.writeValueAsString(expectedQuestionOutput)));

		assertNotNull(questionnaireToBeSaved);
		ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
				() -> questionnaireService.addQuestionToQuestionnaire(questionnaireToBeSaved.getId(), questionId,
						"invalidPageId"));

		assertEquals("Page not found", resourceNotFoundException.getMessage());
	}

	@Test
	public void testAddQuestionWithInvalidQuestionId() throws URISyntaxException {
		String questionnaireId = "654c0048d7db1379df7e7e7e";
		String title = "Single Answer type Question";
		QuestionType questionType = QuestionType.SINGLE_ANSWER;

		Questionnaire questionnaireToBeSaved = getQuestionnaire(title, questionType, questionnaireId);

		String invalidQuestionId = "invalidQuestionId";

		mockServer.expect(ExpectedCount.once(), requestTo(new URI(findQuestionUrl + invalidQuestionId)))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatusCode.valueOf(HttpStatus.SC_NOT_FOUND)).body("Question not found")
						.contentType(MediaType.APPLICATION_JSON));

		assertNotNull(questionnaireToBeSaved);
		HttpClientErrorException resourceNotFoundException = assertThrows(HttpClientErrorException.class,
				() -> questionnaireService.addQuestionToQuestionnaire(questionnaireToBeSaved.getId(), invalidQuestionId,
						questionnaireToBeSaved.getPages().get(0).getId().toString()));

		assertEquals("404 Not Found: \"Question not found\"", resourceNotFoundException.getMessage());
	}

	private Questionnaire getQuestionnaire(String title, QuestionType questionType, String questionnaireId) {
		Question existingQuestion = Question.builder().label("Label 01").type(QuestionType.SINGLE_ANSWER.toString())
				.group("").optionsSource(null).validations(null).editable(false).build();

		Question questionToBeAdded = Question.builder().label(title).type(questionType.toString()).optionsSource(null)
				.validations(null).editable(true).build();

		ArrayList<Question> questions = new ArrayList<>();
		questions.add(existingQuestion);
		questions.add(questionToBeAdded);

		QuestionnairePage questionnairePage = QuestionnairePage.builder().title("Page Title 01").questions(questions)
				.build();

		ArrayList<QuestionnairePage> pages = new ArrayList<>();
		pages.add(questionnairePage);

		Questionnaire questionnaireToBeSaved = Questionnaire.builder().id(questionnaireId).title("Title 01")
				.pages(pages).build();

		Optional<Questionnaire> savedQuestionnaire = Optional.ofNullable(questionnaireToBeSaved);

		doReturn(savedQuestionnaire).when(questionnaireRepository).findById(questionnaireId);
		return questionnaireToBeSaved;
	}
}