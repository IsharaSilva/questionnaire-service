package com.xitricon.questionnaireservice.repository.v2;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.xitricon.questionnaireservice.model.v2.QuestionnaireQuestion;

public interface QuestionnaireQuestionRepository extends MongoRepository<QuestionnaireQuestion, String> {

}
