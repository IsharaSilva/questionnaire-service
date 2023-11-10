package com.xitricon.questionnaireservice.v2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import com.xitricon.questionnaireservice.dto.v2.QuestionnaireInputDTO;
import com.xitricon.questionnaireservice.dto.v2.QuestionnaireQuestionInputDTO;

import com.xitricon.questionnaireservice.repository.v2.QuestionnaireV2Repository;


import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureDataMongo
public class QuestionnaireQuestionControllerTest {

    // private static final String QUESTIONNAIREQUESTION_PATH = "/api/v2/questionnaires";
    // private static final String QUESTIONNAIREQUESTION_ID_PATH = "/api/v2/questionnaires/{id}";

    public static final int OK_STATUS_CODE= 200;

    public static final int CREATED_STATUS_CODE = 201;
    public static final int NOTFOUND_STATUS_CODE = 400;
    public static final int NOCONTENT_STATUS_CODE = 204;

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
            new QuestionnaireQuestionInputDTO("question2", "dependsOn2", "determinator2")
        );
    
        QuestionnaireInputDTO inputDTO = new QuestionnaireInputDTO("Sample Questionnaire", questionList);
    
        given()
                .contentType(ContentType.JSON)
                .body(inputDTO)
                .when()
                .post("/api/v2/questionnaires")
                .then()
                .statusCode(CREATED_STATUS_CODE)
                .body("title", equalTo("Sample Questionnaire"))
                .body("id", notNullValue())
                .body("createdAt", notNullValue());
    }
    
    
    
}