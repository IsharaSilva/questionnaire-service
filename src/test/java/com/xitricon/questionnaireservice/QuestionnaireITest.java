package com.xitricon.questionnaireservice;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xitricon.questionnaireservice.dto.QuestionOutputDTO;
import com.xitricon.questionnaireservice.dto.QuestionServiceOutputDTO;
import com.xitricon.questionnaireservice.dto.QuestionnaireOutputDTO;
import com.xitricon.questionnaireservice.model.Question;
import com.xitricon.questionnaireservice.model.QuestionnairePage;
import com.xitricon.questionnaireservice.model.enums.QuestionType;
import com.xitricon.questionnaireservice.service.QuestionnaireService;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.xitricon.questionnaireservice.model.Questionnaire;
import com.xitricon.questionnaireservice.repository.QuestionnaireRepository;
import com.xitricon.questionnaireservice.testutils.TestConstants;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureDataMongo
public class QuestionnaireITest {
	private static final String QUESTIONNAIRE_RESOURCE = "/api/questionnaires/{id}";

	@Autowired
	QuestionnaireRepository questionnaireRepository;

	@Autowired
	QuestionnaireService questionnaireService;

	@Autowired
	RestTemplate restTemplate;

	@Value("${external-api.question-service.find-by-id}")
	private String findQuestionUrl;

	private MockRestServiceServer mockServer;
	private final ObjectMapper mapper = new ObjectMapper();

	@LocalServerPort
	private int port;

	@BeforeEach
	public void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@AfterEach
	public void cleanUp() {
		this.questionnaireRepository.deleteAll();
	}

	@Test
	public void testGetQuestionnaireById() {

		Questionnaire savedQuestionnaire = questionnaireRepository.findAll().get(0);

		RestAssured.given().contentType(ContentType.JSON).pathParam("id", savedQuestionnaire.getId())
				.get(QUESTIONNAIRE_RESOURCE).then().statusCode(HttpStatus.SC_OK)
				.body("id", equalTo(savedQuestionnaire.getId())).body("title", equalTo(savedQuestionnaire.getTitle()))
				.body("createdAt",
						equalTo(savedQuestionnaire.getCreatedAt()
								.format(DateTimeFormatter.ofPattern(TestConstants.DATE_TIME_FORMAT))))
				.body("modifiedAt",
						equalTo(savedQuestionnaire.getModifiedAt()
								.format(DateTimeFormatter.ofPattern(TestConstants.DATE_TIME_FORMAT))))
				.body("createdBy", equalTo(savedQuestionnaire.getCreatedBy()))
				.body("modifiedBy", equalTo(savedQuestionnaire.getModifiedBy()))
				.body("pages.size()", equalTo(savedQuestionnaire.getPages().size()))
				.body("pages[0].id", equalTo(savedQuestionnaire.getPages().get(0).getId().toString()))
				.body("pages[0].index", equalTo(savedQuestionnaire.getPages().get(0).getIndex()))
				.body("pages[0].title", equalTo(savedQuestionnaire.getPages().get(0).getTitle()))
				.body("pages[0].questions.size()", equalTo(savedQuestionnaire.getPages().get(0).getQuestions().size()))
				.body("pages[0].questions[0].id",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(0).getId().toString()))
				.body("pages[0].questions[0].index",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(0).getIndex()))
				.body("pages[0].questions[0].label",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(0).getLabel()))
				.body("pages[0].questions[0].type",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(0).getType()))
				.body("pages[0].questions[0].group",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(0).getGroup()))
				.body("pages[0].questions[0].validations[0].required",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(0).getValidations().get(0).isRequired()))
				.body("pages[0].questions[0].editable",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(0).isEditable()))
				.body("pages[0].questions[0].optionsSource", notNullValue())
				.body("pages[0].questions[0].subQuestions", notNullValue())
				.body("pages[0].questions[1].id",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(1).getId().toString()))
				.body("pages[0].questions[1].index",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(1).getIndex()))
				.body("pages[0].questions[1].label",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(1).getLabel()))
				.body("pages[0].questions[1].type",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(1).getType()))
				.body("pages[0].questions[1].group",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(1).getGroup()))
				.body("pages[0].questions[1].validations[0].required",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(1).getValidations().get(0).isRequired()))
				.body("pages[0].questions[1].editable",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(1).isEditable()))
				.body("pages[0].questions[1].optionsSource", notNullValue())
				.body("pages[0].questions[1].subQuestions", notNullValue())
				.body("pages[0].questions[2].id",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(2).getId().toString()))
				.body("pages[0].questions[2].index",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(2).getIndex()))
				.body("pages[0].questions[2].label",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(2).getLabel()))
				.body("pages[0].questions[2].type",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(2).getType()))
				.body("pages[0].questions[2].group",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(2).getGroup()))
				.body("pages[0].questions[2].validations[0].required",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(2).getValidations().get(0).isRequired()))
				.body("pages[0].questions[2].editable",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(2).isEditable()))
				.body("pages[0].questions[2].optionsSource", notNullValue())
				.body("pages[0].questions[2].subQuestions", notNullValue())
				.body("pages[0].questions[3].id",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(3).getId().toString()))
				.body("pages[0].questions[3].index",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(3).getIndex()))
				.body("pages[0].questions[3].label",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(3).getLabel()))
				.body("pages[0].questions[3].type",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(3).getType()))
				.body("pages[0].questions[3].group",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(3).getGroup()))
				.body("pages[0].questions[3].validations[0].required",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(3).getValidations().get(0).isRequired()))
				.body("pages[0].questions[3].editable",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(3).isEditable()))
				.body("pages[0].questions[3].optionsSource", notNullValue())
				.body("pages[0].questions[3].subQuestions", notNullValue())
				.body("pages[0].questions[4].id",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(4).getId().toString()))
				.body("pages[0].questions[4].index",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(4).getIndex()))
				.body("pages[0].questions[4].label",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(4).getLabel()))
				.body("pages[0].questions[4].type",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(4).getType()))
				.body("pages[0].questions[4].group",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(4).getGroup()))
				.body("pages[0].questions[4].validations[0].required",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(4).getValidations().get(0).isRequired()))
				.body("pages[0].questions[4].editable",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(4).isEditable()))
				.body("pages[0].questions[4].optionsSource", notNullValue())
				.body("pages[0].questions[4].subQuestions", notNullValue())
				.body("pages[0].questions[5].id",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(5).getId().toString()))
				.body("pages[0].questions[5].index",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(5).getIndex()))
				.body("pages[0].questions[5].label",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(5).getLabel()))
				.body("pages[0].questions[5].type",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(5).getType()))
				.body("pages[0].questions[5].group",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(5).getGroup()))
				.body("pages[0].questions[5].validations[0].required",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(5).getValidations().get(0).isRequired()))
				.body("pages[0].questions[5].editable",
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(5).isEditable()))
				.body("pages[0].questions[5].optionsSource", notNullValue())
				.body("pages[0].questions[5].subQuestions", notNullValue())

				.body("pages[1].id", equalTo(savedQuestionnaire.getPages().get(1).getId().toString()))
				.body("pages[1].index", equalTo(savedQuestionnaire.getPages().get(1).getIndex()))
				.body("pages[1].title", equalTo(savedQuestionnaire.getPages().get(1).getTitle()))
				.body("pages[1].questions.size()", equalTo(savedQuestionnaire.getPages().get(1).getQuestions().size()))
				.body("pages[1].questions[0].id",
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(0).getId().toString()))
				.body("pages[1].questions[0].index",
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(0).getIndex()))
				.body("pages[1].questions[0].label",
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(0).getLabel()))
				.body("pages[1].questions[0].type",
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(0).getType()))
				.body("pages[1].questions[0].group",
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(0).getGroup()))
				.body("pages[1].questions[0].validations[0].required",
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(0).getValidations().get(0).isRequired()))
				.body("pages[1].questions[0].editable",
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(0).isEditable()))
				.body("pages[1].questions[0].optionsSource", notNullValue())
				.body("pages[1].questions[0].subQuestions", notNullValue())
				.body("pages[1].questions[1].id",
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(1).getId().toString()))
				.body("pages[1].questions[1].index",
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(1).getIndex()))
				.body("pages[1].questions[1].label",
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(1).getLabel()))
				.body("pages[1].questions[1].type",
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(1).getType()))
				.body("pages[1].questions[1].group",
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(1).getGroup()))
				.body("pages[1].questions[1].validations[0].required",
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(1).getValidations().get(0).isRequired()))
				.body("pages[1].questions[1].editable",
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(1).isEditable()))
				.body("pages[1].questions[1].optionsSource", notNullValue())
				.body("pages[1].questions[1].subQuestions", notNullValue())
				.body("pages[1].questions[2].id",
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(2).getId().toString()))
				.body("pages[1].questions[2].index",
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(2).getIndex()))
				.body("pages[1].questions[2].label",
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(2).getLabel()))
				.body("pages[1].questions[2].type",
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(2).getType()))
				.body("pages[1].questions[2].group",
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(2).getGroup()))
				.body("pages[1].questions[2].validations[0].required",
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(2).getValidations().get(0).isRequired()))
				.body("pages[1].questions[2].editable",
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(2).isEditable()))
				.body("pages[1].questions[2].optionsSource", notNullValue())
				.body("pages[1].questions[2].subQuestions", notNullValue())

				.body("pages[2].id", equalTo(savedQuestionnaire.getPages().get(2).getId().toString()))
				.body("pages[2].index", equalTo(savedQuestionnaire.getPages().get(2).getIndex()))
				.body("pages[2].title", equalTo(savedQuestionnaire.getPages().get(2).getTitle()))
				.body("pages[2].questions.size()", equalTo(savedQuestionnaire.getPages().get(2).getQuestions().size()))
				.body("pages[2].questions[0].id",
						equalTo(savedQuestionnaire.getPages().get(2).getQuestions().get(0).getId().toString()))
				.body("pages[2].questions[0].index",
						equalTo(savedQuestionnaire.getPages().get(2).getQuestions().get(0).getIndex()))
				.body("pages[2].questions[0].label",
						equalTo(savedQuestionnaire.getPages().get(2).getQuestions().get(0).getLabel()))
				.body("pages[2].questions[0].type",
						equalTo(savedQuestionnaire.getPages().get(2).getQuestions().get(0).getType()))
				.body("pages[2].questions[0].group",
						equalTo(savedQuestionnaire.getPages().get(2).getQuestions().get(0).getGroup()))
				.body("pages[2].questions[0].validations[0].required",
						equalTo(savedQuestionnaire.getPages().get(2).getQuestions().get(0).getValidations().get(0).isRequired()))
				.body("pages[2].questions[0].editable",
						equalTo(savedQuestionnaire.getPages().get(2).getQuestions().get(0).isEditable()))
				.body("pages[2].questions[0].optionsSource", notNullValue())
				.body("pages[2].questions[0].subQuestions", notNullValue())
				.body("pages[2].questions[1].id",
						equalTo(savedQuestionnaire.getPages().get(2).getQuestions().get(1).getId().toString()))
				.body("pages[2].questions[1].index",
						equalTo(savedQuestionnaire.getPages().get(2).getQuestions().get(1).getIndex()))
				.body("pages[2].questions[1].label",
						equalTo(savedQuestionnaire.getPages().get(2).getQuestions().get(1).getLabel()))
				.body("pages[2].questions[1].type",
						equalTo(savedQuestionnaire.getPages().get(2).getQuestions().get(1).getType()))
				.body("pages[2].questions[1].group",
						equalTo(savedQuestionnaire.getPages().get(2).getQuestions().get(1).getGroup()))
				.body("pages[2].questions[1].validations[0].required",
						equalTo(savedQuestionnaire.getPages().get(2).getQuestions().get(1).getValidations().get(0).isRequired()))
				.body("pages[2].questions[1].editable",
						equalTo(savedQuestionnaire.getPages().get(2).getQuestions().get(1).isEditable()))
				.body("pages[2].questions[1].optionsSource", notNullValue())
				.body("pages[2].questions[1].subQuestions", notNullValue())

				.body("pages[3].id", equalTo(savedQuestionnaire.getPages().get(3).getId().toString()))
				.body("pages[3].index", equalTo(savedQuestionnaire.getPages().get(3).getIndex()))
				.body("pages[3].title", equalTo(savedQuestionnaire.getPages().get(3).getTitle()))
				.body("pages[3].questions.size()", equalTo(savedQuestionnaire.getPages().get(3).getQuestions().size()))
				.body("pages[3].questions[0].id",
						equalTo(savedQuestionnaire.getPages().get(3).getQuestions().get(0).getId().toString()))
				.body("pages[3].questions[0].index",
						equalTo(savedQuestionnaire.getPages().get(3).getQuestions().get(0).getIndex()))
				.body("pages[3].questions[0].label",
						equalTo(savedQuestionnaire.getPages().get(3).getQuestions().get(0).getLabel()))
				.body("pages[3].questions[0].type",
						equalTo(savedQuestionnaire.getPages().get(3).getQuestions().get(0).getType()))
				.body("pages[3].questions[0].group",
						equalTo(savedQuestionnaire.getPages().get(3).getQuestions().get(0).getGroup()))
				.body("pages[3].questions[0].validations[0].required",
						equalTo(savedQuestionnaire.getPages().get(3).getQuestions().get(0).getValidations().get(0).isRequired()))
				.body("pages[3].questions[0].editable",
						equalTo(savedQuestionnaire.getPages().get(3).getQuestions().get(0).isEditable()))
				.body("pages[3].questions[0].optionsSource", notNullValue())
				.body("pages[3].questions[0].subQuestions", notNullValue())

				.body("pages[4].id", equalTo(savedQuestionnaire.getPages().get(4).getId().toString()))
				.body("pages[4].index", equalTo(savedQuestionnaire.getPages().get(4).getIndex()))
				.body("pages[4].title", equalTo(savedQuestionnaire.getPages().get(4).getTitle()))
				.body("pages[4].questions.size()", equalTo(savedQuestionnaire.getPages().get(4).getQuestions().size()))
				.body("pages[4].questions[0].id",
						equalTo(savedQuestionnaire.getPages().get(4).getQuestions().get(0).getId().toString()))
				.body("pages[4].questions[0].index",
						equalTo(savedQuestionnaire.getPages().get(4).getQuestions().get(0).getIndex()))
				.body("pages[4].questions[0].label",
						equalTo(savedQuestionnaire.getPages().get(4).getQuestions().get(0).getLabel()))
				.body("pages[4].questions[0].type",
						equalTo(savedQuestionnaire.getPages().get(4).getQuestions().get(0).getType()))
				.body("pages[4].questions[0].group",
						equalTo(savedQuestionnaire.getPages().get(4).getQuestions().get(0).getGroup()))
				.body("pages[4].questions[0].validations[0].required",
						equalTo(savedQuestionnaire.getPages().get(4).getQuestions().get(0).getValidations().get(0).isRequired()))
				.body("pages[4].questions[0].editable",
						equalTo(savedQuestionnaire.getPages().get(4).getQuestions().get(0).isEditable()))
				.body("pages[4].questions[0].optionsSource", notNullValue())
				.body("pages[4].questions[0].subQuestions", notNullValue());

	}

	@Test
	public void testAddQuestionToQuestionnaire() throws URISyntaxException, JsonProcessingException {
		Question question = Question.builder().label("Label 01").type(QuestionType.SINGLE_ANSWER.toString()).group("").optionsSource(null)
				.validations(null).editable(false).build();

		QuestionnairePage questionnairePage = QuestionnairePage.builder().title("Page Title 01").questions(List.of(question))
				.build();

		Questionnaire savedQuestionnaire = this.questionnaireRepository
				.save(Questionnaire.builder().title("Title 01").pages(List.of(questionnairePage)).build());

		String questionId = "654c0048d7db1379df7e7f1e";
		String title = "Single Answer type Question";
		QuestionType questionType = QuestionType.SINGLE_ANSWER;

		QuestionServiceOutputDTO expectedQuestionOutput = new QuestionServiceOutputDTO(questionId, title, questionType, new ArrayList<>());

		mockServer.expect(ExpectedCount.once(),
				requestTo(new URI(findQuestionUrl + questionId)))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatusCode.valueOf(HttpStatus.SC_OK))
						.contentType(MediaType.APPLICATION_JSON)
						.body(mapper.writeValueAsString(expectedQuestionOutput))
				);

		QuestionnaireOutputDTO questionnaireOutputDTO = questionnaireService
				.addQuestionToQuestionnaire(savedQuestionnaire.getId(), questionId, savedQuestionnaire.getPages().get(0).getId().toString());

		QuestionOutputDTO savedQuestion = questionnaireOutputDTO.getPages().get(0).getQuestions().stream()
				.filter(questionOutputDTO -> questionOutputDTO.getId().equals(questionId)).findFirst().orElse(null);

		assertThat(savedQuestion, is(notNullValue()));
	}

	@Test
	public void testGetQuestionnaireByNonExistingId() {

		RestAssured.given().contentType(ContentType.JSON).pathParam("id", "test").get(QUESTIONNAIRE_RESOURCE).then()
				.statusCode(HttpStatus.SC_NOT_FOUND);
	}

}
