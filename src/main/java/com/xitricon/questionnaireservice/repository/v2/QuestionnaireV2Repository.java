package com.xitricon.questionnaireservice.repository.v2;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.xitricon.questionnaireservice.model.v2.Questionnaire;

import java.util.Optional;

public interface QuestionnaireV2Repository extends MongoRepository<Questionnaire, String> {

    Optional<Questionnaire> findByTenantIdAndId(String tenantId, String Id);
}
