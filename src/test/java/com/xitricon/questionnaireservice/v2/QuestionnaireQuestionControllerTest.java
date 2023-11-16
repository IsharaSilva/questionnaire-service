package com.xitricon.questionnaireservice.v2;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
import io.restassured.response.Response;




@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureDataMongo
public class QuestionnaireQuestionControllerTest {

	private static final String QuestionnaireQuestion_PATH = "/api/v2/questionnaires";
	private static final String QuestionnaireQuestion_ID_PATH = "/api/v2/questionnaires/{id}";

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
	public void testCreateQuestionnaire() {
		List<QuestionnaireQuestionInputDTO> questionList = Arrays.asList(
				new QuestionnaireQuestionInputDTO("question1", "dependsOn1", "determinator1"),
				new QuestionnaireQuestionInputDTO("question2", "dependsOn2", "determinator2"));

		QuestionnaireInputDTO inputDTO = new QuestionnaireInputDTO("Sample Questionnaire", questionList);

		given().contentType(ContentType.JSON).body(inputDTO).when().post(QuestionnaireQuestion_PATH).then()
				.statusCode(HttpStatus.SC_CREATED).body("title", equalTo("Sample Questionnaire"))
				.body("id", notNullValue()).body("createdAt", notNullValue()).body("modifiedAt", notNullValue())
				.body("createdBy", notNullValue()).body("modifiedBy", notNullValue());
	}


	@Test
	public void testGetQuestionnaireById() {
		List<QuestionnaireQuestionInputDTO> questionList = Arrays.asList(
				new QuestionnaireQuestionInputDTO("question1", "dependsOn1", "determinator1"),
				new QuestionnaireQuestionInputDTO("question2", "dependsOn2", "determinator2"));

		QuestionnaireInputDTO inputDTO = new QuestionnaireInputDTO("Sample Questionnaire", questionList);

		String respString = RestAssured.given().contentType(ContentType.JSON).body(inputDTO)
				.post(QuestionnaireQuestion_PATH).then().statusCode(HttpStatus.SC_CREATED).extract().asString();

		String id = JsonPath.from(respString).get("id");

		Response response = RestAssured.given().contentType(ContentType.JSON).pathParam("id", id).get(QuestionnaireQuestion_ID_PATH);

		response.then().statusCode(HttpStatus.SC_OK)
				.body("title", equalTo(inputDTO.getTitle()))
				.body("createdAt", notNullValue())
				.body("modifiedAt", notNullValue())
				.body("createdBy", notNullValue())
				.body("modifiedBy", notNullValue());

		List<Map<String, String>> questionsFromResponse = response.jsonPath().getList("questions");

		// Compare the individual properties of the questions
		for (int i = 0; i < questionList.size(); i++) {
			QuestionnaireQuestionInputDTO expectedQuestion = questionList.get(i);
			Map<String, String> actualQuestion = questionsFromResponse.get(i);

			assertThat(actualQuestion.get("questionRef"), equalTo(expectedQuestion.getQuestionRef()));
			assertThat(actualQuestion.get("dependsOn"), equalTo(expectedQuestion.getDependsOn()));
			assertThat(actualQuestion.get("determinator"), equalTo(expectedQuestion.getDeterminator()));
		}
	}
	
	@Test
	public void testUpdateQuestionnaire() {
		// Create an initial questionnaire
		List<QuestionnaireQuestionInputDTO> initialQuestionList = Arrays.asList(
				new QuestionnaireQuestionInputDTO("question1", "dependsOn1", "determinator1"),
				new QuestionnaireQuestionInputDTO("question2", "dependsOn2", "determinator2"));

		QuestionnaireInputDTO inputDTO = new QuestionnaireInputDTO("Sample Questionnaire", initialQuestionList);

		String respString = RestAssured.given().contentType(ContentType.JSON).body(inputDTO)
				.post(QuestionnaireQuestion_PATH).then().statusCode(HttpStatus.SC_CREATED).extract().asString();


		String id = JsonPath.from(respString).get("id");

		Response response = RestAssured.given().contentType(ContentType.JSON).pathParam("id", id).get(QuestionnaireQuestion_ID_PATH);

		response.then().statusCode(HttpStatus.SC_OK)
				.body("title", equalTo(inputDTO.getTitle()))
				.body("createdAt", notNullValue())
				.body("modifiedAt", notNullValue())
				.body("createdBy", notNullValue())
				.body("modifiedBy", notNullValue());

		List<Map<String, String>> questionsFromResponse = response.jsonPath().getList("questions");

		for (int i = 0; i < initialQuestionList.size(); i++) {
			QuestionnaireQuestionInputDTO expectedQuestion = initialQuestionList.get(i);
			Map<String, String> actualQuestion = questionsFromResponse.get(i);

			assertThat(actualQuestion.get("questionRef"), equalTo(expectedQuestion.getQuestionRef()));
			assertThat(actualQuestion.get("dependsOn"), equalTo(expectedQuestion.getDependsOn()));
			assertThat(actualQuestion.get("determinator"), equalTo(expectedQuestion.getDeterminator()));
		}
	}

	@Test
	public void testGetQuestionnaireByIdNotFound() {
		given().contentType(ContentType.JSON).when().get(QuestionnaireQuestion_ID_PATH, "1").then()
				.statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void testUpdateQuestionnaireNotFound() {
		List<QuestionnaireQuestionInputDTO> questionList = Arrays.asList(
				new QuestionnaireQuestionInputDTO("question1", "dependsOn1", "determinator1"),
				new QuestionnaireQuestionInputDTO("question2", "dependsOn2", "determinator2"));

		QuestionnaireInputDTO inputDTO = new QuestionnaireInputDTO("Sample Questionnaire", questionList);

		given().contentType(ContentType.JSON).body(inputDTO).when().put(QuestionnaireQuestion_ID_PATH, "1").then()
				.statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void testUpdateQuestionnaireQuestions() {
		List<QuestionnaireQuestionInputDTO> questionList = Arrays.asList(
				new QuestionnaireQuestionInputDTO("question1", "dependsOn1", "determinator1"),
				new QuestionnaireQuestionInputDTO("question2", "dependsOn2", "determinator2"));

		QuestionnaireInputDTO inputDTO = new QuestionnaireInputDTO("Sample Questionnaire", questionList);

		String respString = RestAssured.given().contentType(ContentType.JSON).body(inputDTO)
				.post(QuestionnaireQuestion_PATH).then().statusCode(HttpStatus.SC_CREATED).extract().asString();

		String id = JsonPath.from(respString).get("id");

        QuestionnaireQuestionUpdateInputDTO updateDTO = new QuestionnaireQuestionUpdateInputDTO(
            Arrays.asList("question3", "question4"),
            Arrays.asList("question1", "question2")
        );
    
        RestAssured.given().contentType(ContentType.JSON).pathParam("id", id)
                .body(updateDTO)  
                .put(QuestionnaireQuestion_ID_PATH + "/questions")
                .then().statusCode(HttpStatus.SC_OK)
                .body("questions.size()", equalTo(4));
    }

	@Test
	public void testUpdateQuestionnaireQuestionsNotFound() {


        QuestionnaireQuestionUpdateInputDTO updateDTO = new QuestionnaireQuestionUpdateInputDTO(
            Arrays.asList("question3", "question4"),
            Arrays.asList("question1", "question2")
        );

		given().contentType(ContentType.JSON)
				.body(updateDTO)  
				.when().put(QuestionnaireQuestion_ID_PATH + "/questions", "1").then()
				.statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void testUpdateQuestionnaireQuestionsBadRequest() {

        QuestionnaireQuestionUpdateInputDTO updateDTO = new QuestionnaireQuestionUpdateInputDTO(
            Arrays.asList("question3", "question4"),
            Arrays.asList("question1", "question2")
        );

		given().contentType(ContentType.JSON)
				.body(updateDTO)  
				.when().put(QuestionnaireQuestion_ID_PATH + "/questions", "1").then()
				.statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void testCreateQuestionnaireBadRequest() {
		List<QuestionnaireQuestionInputDTO> questionList = Arrays.asList(
				new QuestionnaireQuestionInputDTO("question1", "dependsOn1", "determinator1"),
				new QuestionnaireQuestionInputDTO("question2", "dependsOn2", "determinator2"));

		QuestionnaireInputDTO inputDTO = new QuestionnaireInputDTO("Sample Questionnaire", questionList);

		given().contentType(ContentType.JSON).body(inputDTO).when().post(QuestionnaireQuestion_PATH).then()
				.statusCode(HttpStatus.SC_CREATED).body("title", equalTo("Sample Questionnaire"))
				.body("id", notNullValue()).body("createdAt", notNullValue());
	}

	@Test
	public void testUpdateQuestionnaireBadRequest() {
		List<QuestionnaireQuestionInputDTO> questionList = Arrays.asList(
				new QuestionnaireQuestionInputDTO("question1", "dependsOn1", "determinator1"),
				new QuestionnaireQuestionInputDTO("question2", "dependsOn2", "determinator2"));

		QuestionnaireInputDTO inputDTO = new QuestionnaireInputDTO("Sample Questionnaire", questionList);

		String respString = RestAssured.given().contentType(ContentType.JSON).body(inputDTO)
				.post(QuestionnaireQuestion_PATH).then().statusCode(HttpStatus.SC_CREATED).extract().asString();

		String id = JsonPath.from(respString).get("id");

		List<QuestionnaireQuestionInputDTO> questionList2 = Arrays.asList(
				new QuestionnaireQuestionInputDTO("question3", "dependsOn3", "determinator3"),
				new QuestionnaireQuestionInputDTO("question4", "dependsOn4", "determinator4"));

		QuestionnaireInputDTO inputDTO2 = new QuestionnaireInputDTO("Sample Questionnaire2", questionList2);

		RestAssured.given().contentType(ContentType.JSON).pathParam("id", id).body(inputDTO2)
				.put(QuestionnaireQuestion_ID_PATH).then().statusCode(HttpStatus.SC_OK)
				.body("title", equalTo(inputDTO2.getTitle()));
	}

	@Test
	public void testGetQuestionnaireByIdBadRequest() {
		given().contentType(ContentType.JSON).when().get(QuestionnaireQuestion_ID_PATH, "1").then()
				.statusCode(HttpStatus.SC_NOT_FOUND);
	}

}