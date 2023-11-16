package com.xitricon.questionnaireservice;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.time.format.DateTimeFormatter;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.xitricon.questionnaireservice.model.Questionnaire;
import com.xitricon.questionnaireservice.repository.QuestionnaireRepository;
import com.xitricon.questionnaireservice.utils.TestConstants;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureDataMongo
public class QuestionnaireITest {
	private static final String QUESTIONNAIRE_RESOURCE = "/api/questionnaires/{id}";

	@Autowired
	QuestionnaireRepository questionnaireRepository;

	@LocalServerPort
	private int port;

	@BeforeEach
	public void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
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
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(0).getValidations().get(0)
								.isRequired()))
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
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(1).getValidations().get(0)
								.isRequired()))
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
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(2).getValidations().get(0)
								.isRequired()))
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
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(3).getValidations().get(0)
								.isRequired()))
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
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(4).getValidations().get(0)
								.isRequired()))
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
						equalTo(savedQuestionnaire.getPages().get(0).getQuestions().get(5).getValidations().get(0)
								.isRequired()))
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
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(0).getValidations().get(0)
								.isRequired()))
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
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(1).getValidations().get(0)
								.isRequired()))
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
						equalTo(savedQuestionnaire.getPages().get(1).getQuestions().get(2).getValidations().get(0)
								.isRequired()))
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
						equalTo(savedQuestionnaire.getPages().get(2).getQuestions().get(0).getValidations().get(0)
								.isRequired()))
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
						equalTo(savedQuestionnaire.getPages().get(2).getQuestions().get(1).getValidations().get(0)
								.isRequired()))
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
						equalTo(savedQuestionnaire.getPages().get(3).getQuestions().get(0).getValidations().get(0)
								.isRequired()))
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
						equalTo(savedQuestionnaire.getPages().get(4).getQuestions().get(0).getValidations().get(0)
								.isRequired()))
				.body("pages[4].questions[0].editable",
						equalTo(savedQuestionnaire.getPages().get(4).getQuestions().get(0).isEditable()))
				.body("pages[4].questions[0].optionsSource", notNullValue())
				.body("pages[4].questions[0].subQuestions", notNullValue());

	}

	@Test
	public void testGetQuestionnaireByNonExistingId() {

		RestAssured.given().contentType(ContentType.JSON).pathParam("id", "test").get(QUESTIONNAIRE_RESOURCE).then()
				.statusCode(HttpStatus.SC_NOT_FOUND);
	}

}
