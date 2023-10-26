package com.xitricon.questionnaireservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.xitricon.questionnaireservice.model.Questionnaire;

public interface QuestionnaireRepository extends MongoRepository<Questionnaire, String> {

}
