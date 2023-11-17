package com.xitricon.questionnaireservice.v2;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.xitricon.questionnaireservice.dto.v2.QuestionnaireInputDTO;
import com.xitricon.questionnaireservice.dto.v2.QuestionnaireQuestionInputDTO;
import com.xitricon.questionnaireservice.dto.v2.QuestionnaireQuestionUpdateInputDTO;
import com.xitricon.questionnaireservice.repository.v2.QuestionnaireV2Repository;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureDataMongo
class QuestionnaireQuestionControllerTest {

	private static final String QuestionnaireQuestion_PATH = "/api/v2/questionnaires";
	private static final String QuestionnaireQuestion_ID_PATH = "/api/v2/questionnaires/{id}";

	private final String tenantId = "001";

	@Autowired
	QuestionnaireV2Repository questionnaireV2Repository;

	@LocalServerPort
	private int port;

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@AfterEach
	void cleanUp() {
		this.questionnaireV2Repository.deleteAll();
	}

	@Test
	void testCreateQuestionnaire() {
		List<QuestionnaireQuestionInputDTO> questionList = Arrays.asList(
				new QuestionnaireQuestionInputDTO(tenantId, "question1", "dependsOn1", "determinator1"),
				new QuestionnaireQuestionInputDTO(tenantId, "question2", "dependsOn2", "determinator2"));

		QuestionnaireInputDTO inputDTO = new QuestionnaireInputDTO(tenantId, "Sample Questionnaire", questionList);

		given().contentType(ContentType.JSON).body(inputDTO).queryParam("tenantId", tenantId).when()
				.post(QuestionnaireQuestion_PATH).then().statusCode(HttpStatus.SC_CREATED)
				.body("title", equalTo("Sample Questionnaire")).body("id", notNullValue())
				.body("createdAt", notNullValue()).body("modifiedAt", notNullValue()).body("createdBy", notNullValue())
				.body("modifiedBy", notNullValue());
	}

	@Test
	void testGetQuestionnaireById() {
		List<QuestionnaireQuestionInputDTO> questionList = Arrays.asList(
				new QuestionnaireQuestionInputDTO(tenantId, "question1", "dependsOn1", "determinator1"),
				new QuestionnaireQuestionInputDTO(tenantId, "question2", "dependsOn2", "determinator2"));

		QuestionnaireInputDTO inputDTO = new QuestionnaireInputDTO(tenantId, "Sample Questionnaire", questionList);

		String respString = RestAssured.given().contentType(ContentType.JSON).body(inputDTO)
				.queryParam("tenantId", tenantId).post(QuestionnaireQuestion_PATH).then()
				.statusCode(HttpStatus.SC_CREATED).extract().asString();

		String id = JsonPath.from(respString).get("id");

		RestAssured.given().contentType(ContentType.JSON).pathParam("id", id).queryParam("tenantId", tenantId).body(inputDTO)
				.put(QuestionnaireQuestion_ID_PATH).then().statusCode(HttpStatus.SC_OK)
				.body("title", equalTo(inputDTO.getTitle())).body("createdAt", notNullValue())
				.body("modifiedAt", notNullValue()).body("createdBy", notNullValue()).body("modifiedBy", notNullValue())
				.body("questions[0].questionRef", equalTo("question1"))
				.body("questions[0].dependsOn", equalTo("dependsOn1"))
				.body("questions[0].determinator", equalTo("determinator1"));
	}

	@Test
	void testUpdateQuestionnaire() {
		// Create an initial questionnaire
		List<QuestionnaireQuestionInputDTO> initialQuestionList = Arrays.asList(
				new QuestionnaireQuestionInputDTO(tenantId, "question1", "dependsOn1", "determinator1"),
				new QuestionnaireQuestionInputDTO(tenantId, "question2", "dependsOn2", "determinator2"));

		QuestionnaireInputDTO inputDTO = new QuestionnaireInputDTO(tenantId, "Sample Questionnaire",
				initialQuestionList);

		String respString = RestAssured.given().contentType(ContentType.JSON).body(inputDTO)
				.queryParam("tenantId", tenantId).post(QuestionnaireQuestion_PATH).then()
				.statusCode(HttpStatus.SC_CREATED).extract().asString();

		String id = JsonPath.from(respString).get("id");

		RestAssured.given().contentType(ContentType.JSON).pathParam("id", id).queryParam("tenantId", tenantId).body(inputDTO)
				.put(QuestionnaireQuestion_ID_PATH).then().statusCode(HttpStatus.SC_OK)
				.body("title", equalTo(inputDTO.getTitle())).body("createdAt", notNullValue())
				.body("modifiedAt", notNullValue()).body("createdBy", notNullValue()).body("modifiedBy", notNullValue())
				.body("questions[0].questionRef", equalTo("question1"))
				.body("questions[0].dependsOn", equalTo("dependsOn1"))
				.body("questions[0].determinator", equalTo("determinator1"));
	}

	@Test
	void testGetQuestionnaireByIdNotFound() {
		given().contentType(ContentType.JSON).queryParam("tenantId", tenantId).when().get(QuestionnaireQuestion_ID_PATH, "1").then()
				.statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	void testUpdateQuestionnaireNotFound() {
		List<QuestionnaireQuestionInputDTO> questionList = Arrays.asList(
				new QuestionnaireQuestionInputDTO(tenantId, "question1", "dependsOn1", "determinator1"),
				new QuestionnaireQuestionInputDTO(tenantId, "question2", "dependsOn2", "determinator2"));

		QuestionnaireInputDTO inputDTO = new QuestionnaireInputDTO(tenantId, "Sample Questionnaire", questionList);

		given().contentType(ContentType.JSON).body(inputDTO).queryParam("tenantId", tenantId).when()
				.put(QuestionnaireQuestion_ID_PATH, "1").then().statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	void testUpdateQuestionnaireQuestions() {
		List<QuestionnaireQuestionInputDTO> questionList = Arrays.asList(
				new QuestionnaireQuestionInputDTO(tenantId, "question1", "dependsOn1", "determinator1"),
				new QuestionnaireQuestionInputDTO(tenantId, "question2", "dependsOn2", "determinator2"));

		QuestionnaireInputDTO inputDTO = new QuestionnaireInputDTO(tenantId, "Sample Questionnaire", questionList);

		String respString = RestAssured.given().contentType(ContentType.JSON).body(inputDTO)
				.queryParam("tenantId", tenantId).post(QuestionnaireQuestion_PATH).then()
				.statusCode(HttpStatus.SC_CREATED).extract().asString();

		String id = JsonPath.from(respString).get("id");

		QuestionnaireQuestionUpdateInputDTO updateDTO = new QuestionnaireQuestionUpdateInputDTO(
				Arrays.asList("question3", "question4"), Arrays.asList("question1", "question2"));

		RestAssured.given().contentType(ContentType.JSON).pathParam("id", id).queryParam("tenantId", tenantId).body(updateDTO)
				.put(QuestionnaireQuestion_ID_PATH + "/questions").then().statusCode(HttpStatus.SC_OK)
				.body("questions.size()", equalTo(4));
	}

	@Test
	void testUpdateQuestionnaireQuestionsNotFound() {

		QuestionnaireQuestionUpdateInputDTO updateDTO = new QuestionnaireQuestionUpdateInputDTO(
				Arrays.asList("question3", "question4"), Arrays.asList("question1", "question2"));

		given().contentType(ContentType.JSON).queryParam("tenantId", tenantId).body(updateDTO).when()
				.put(QuestionnaireQuestion_ID_PATH + "/questions", "1").then().statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	void testUpdateQuestionnaireQuestionsBadRequest() {

		QuestionnaireQuestionUpdateInputDTO updateDTO = new QuestionnaireQuestionUpdateInputDTO(
				Arrays.asList("question3", "question4"), Arrays.asList("question1", "question2"));

		given().contentType(ContentType.JSON).body(updateDTO).queryParam("tenantId", tenantId).when()
				.put(QuestionnaireQuestion_ID_PATH + "/questions", "1").then().statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	void testCreateQuestionnaireBadRequest() {
		List<QuestionnaireQuestionInputDTO> questionList = Arrays.asList(
				new QuestionnaireQuestionInputDTO(tenantId, "question1", "dependsOn1", "determinator1"),
				new QuestionnaireQuestionInputDTO(tenantId, "question2", "dependsOn2", "determinator2"));

		QuestionnaireInputDTO inputDTO = new QuestionnaireInputDTO(tenantId, "Sample Questionnaire", questionList);

		given().contentType(ContentType.JSON).body(inputDTO).queryParam("tenantId", tenantId).when()
				.post(QuestionnaireQuestion_PATH).then().statusCode(HttpStatus.SC_CREATED)
				.body("title", equalTo("Sample Questionnaire")).body("id", notNullValue())
				.body("createdAt", notNullValue());
	}

	@Test
	void testUpdateQuestionnaireBadRequest() {
		List<QuestionnaireQuestionInputDTO> questionList = Arrays.asList(
				new QuestionnaireQuestionInputDTO(tenantId, "question1", "dependsOn1", "determinator1"),
				new QuestionnaireQuestionInputDTO(tenantId, "question2", "dependsOn2", "determinator2"));

		QuestionnaireInputDTO inputDTO = new QuestionnaireInputDTO(tenantId, "Sample Questionnaire", questionList);

		String respString = RestAssured.given().contentType(ContentType.JSON).body(inputDTO)
				.queryParam("tenantId", tenantId).post(QuestionnaireQuestion_PATH).then()
				.statusCode(HttpStatus.SC_CREATED).extract().asString();

		String id = JsonPath.from(respString).get("id");

		List<QuestionnaireQuestionInputDTO> questionList2 = Arrays.asList(
				new QuestionnaireQuestionInputDTO(tenantId, "question3", "dependsOn3", "determinator3"),
				new QuestionnaireQuestionInputDTO(tenantId, "question4", "dependsOn4", "determinator4"));

		QuestionnaireInputDTO inputDTO2 = new QuestionnaireInputDTO(tenantId, "Sample Questionnaire2", questionList2);

		RestAssured.given().contentType(ContentType.JSON).pathParam("id", id).body(inputDTO2)
				.queryParam("tenantId", tenantId).put(QuestionnaireQuestion_ID_PATH).then().statusCode(HttpStatus.SC_OK)
				.body("title", equalTo(inputDTO2.getTitle()));
	}

	@Test
	public void testGetQuestionnaireByIdBadRequest() {
		given().contentType(ContentType.JSON).queryParam("tenantId", tenantId).when()
				.get(QuestionnaireQuestion_ID_PATH, "1").then().statusCode(HttpStatus.SC_NOT_FOUND);
	}

}