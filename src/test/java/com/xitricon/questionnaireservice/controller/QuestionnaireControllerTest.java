package com.xitricon.questionnaireservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.xitricon.questionnaireservice.dto.QuestionOutputDTO;
import com.xitricon.questionnaireservice.dto.QuestionnaireOutputDTO;
import com.xitricon.questionnaireservice.model.Question;
import com.xitricon.questionnaireservice.model.QuestionnairePage;
import com.xitricon.questionnaireservice.model.enums.QuestionType;
import com.xitricon.questionnaireservice.service.QuestionnaireService;

class QuestionnaireControllerTest {

	private QuestionnaireController questionnaireController;

	@Mock
	private QuestionnaireService questionnaireService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		questionnaireController = new QuestionnaireController(questionnaireService);
	}

	@Test
	void addQuestionToQuestionnaire() {
		String questionnaireId = "654c0048d7db1379df7e7e7e";
		String pageId = "654c0048d7db1379df7e8e8e";
		String questionId = "654c0048d7db1379df7e7f1e";
		String title = "Single Answer type Question";
		QuestionType questionType = QuestionType.SINGLE_ANSWER;

		Question question = Question.builder().id(new ObjectId(questionId)).label(title)
				.type(QuestionType.SINGLE_ANSWER.toString()).group("").optionsSource(null).validations(null)
				.editable(false).build();

		QuestionnairePage questionnairePage = QuestionnairePage.builder().id(new ObjectId(pageId))
				.title("Page Title 01").questions(List.of(question)).build();

		QuestionnaireOutputDTO savedQuestionnaire = new QuestionnaireOutputDTO(questionnaireId, "Supplier Onboarding",
				LocalDateTime.now(), LocalDateTime.now(), "System", "System", List.of(questionnairePage.viewAsDTO()));

		doReturn(savedQuestionnaire).when(questionnaireService).addQuestionToQuestionnaire(questionnaireId, questionId,
				pageId);

		ResponseEntity<QuestionnaireOutputDTO> questionnaireOutputDTOResponseEntity = questionnaireController
				.addQuestionToQuestionnaire(questionnaireId, questionId, pageId);

		QuestionnaireOutputDTO body = questionnaireOutputDTOResponseEntity.getBody();
		assertNotNull(body);
		QuestionOutputDTO questionOutput = body.getPages().get(0).getQuestions().stream()
				.filter(questionOutputDTO -> questionOutputDTO.getId().equals(questionId)).findFirst().orElse(null);

		assertNotNull(questionOutput);
		assertEquals(questionOutput.getId(), questionId);
		assertEquals(questionOutput.getLabel(), title);
		assertEquals(questionOutput.getType(), questionType.toString());
	}
}