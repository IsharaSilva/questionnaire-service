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
import com.xitricon.questionnaireservice.repository.v2.QuestionnaireV2Repository;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureDataMongo
public class QuestionnaireQuestionControllerTest {

	private static final String QuestionnaireQuestion_PATH = "/api/v2/questionnaires";
	private static final String QuestionnaireQuestion_ID_PATH = "/api/v2/questionnaires/{id}";

	private final String tenantId = "001";

	@Autowired
	QuestionnaireV2Repository questionnaireV2Repository;

	@LocalServerPort
	private int port;

	@BeforeEach
	public void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@AfterEach
	public void cleanUp() {
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
				.body("createdAt", notNullValue());
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

		RestAssured.given().contentType(ContentType.JSON).queryParam("tenantId", tenantId).pathParam("id", id)
				.get(QuestionnaireQuestion_ID_PATH).then().statusCode(HttpStatus.SC_OK)
				.body("title", equalTo(inputDTO.getTitle()));
	}

	@Test
	void testUpdateQuestionnaire() {
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

		RestAssured.given().contentType(ContentType.JSON).queryParam("tenantId", tenantId).pathParam("id", id)
				.body(inputDTO2).put(QuestionnaireQuestion_ID_PATH).then().statusCode(HttpStatus.SC_OK)
				.body("title", equalTo(inputDTO2.getTitle()));
	}

	@Test
	void testGetQuestionnaireByIdNotFound() {
		given().contentType(ContentType.JSON).queryParam("tenantId", tenantId).when()
				.get(QuestionnaireQuestion_ID_PATH, "1").then().statusCode(HttpStatus.SC_NOT_FOUND);
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

		RestAssured.given().contentType(ContentType.JSON).pathParam("id", id)
				.body("{\n" + "  \"additions\": [\n" + "    \"question3\",\n" + "    \"question4\"\n" + "  ],\n"
						+ "  \"removals\": [\n" + "    \"question1\",\n" + "    \"question2\"\n" + "  ]\n" + "}")
				.queryParam("tenantId", tenantId).put(QuestionnaireQuestion_ID_PATH + "/questions").then()
				.statusCode(HttpStatus.SC_OK).body("questions.size()", equalTo(2));
	}

	@Test
	void testUpdateQuestionnaireQuestionsNotFound() {

		given().contentType(ContentType.JSON)
				.body("{\n" + "  \"additions\": [\n" + "    \"question3\",\n" + "    \"question4\"\n" + "  ],\n"
						+ "  \"removals\": [\n" + "    \"question1\",\n" + "    \"question2\"\n" + "  ]\n" + "}")
				.queryParam("tenantId", tenantId).when().put(QuestionnaireQuestion_ID_PATH + "/questions", "1").then()
				.statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	void testUpdateQuestionnaireQuestionsBadRequest() {

		given().contentType(ContentType.JSON)
				.body("{\n" + "  \"additions\": [\n" + "    \"question3\",\n" + "    \"question4\"\n" + "  ],\n"
						+ "  \"removals\": [\n" + "    \"question1\",\n" + "    \"question2\"\n" + "  ]\n" + "}")
				.queryParam("tenantId", tenantId).when().put(QuestionnaireQuestion_ID_PATH + "/questions", "1").then()
				.statusCode(HttpStatus.SC_NOT_FOUND);
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
	void testGetQuestionnaireByIdBadRequest() {
		given().contentType(ContentType.JSON).queryParam("tenantId", tenantId).when()
				.get(QuestionnaireQuestion_ID_PATH, "1").then().statusCode(HttpStatus.SC_NOT_FOUND);
	}

}