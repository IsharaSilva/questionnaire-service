package com.xitricon.questionnaireservice.repository.v2;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.xitricon.questionnaireservice.model.v2.Questionnaire;

public interface QuestionnaireV2Repository extends MongoRepository<Questionnaire, String> {
	Optional<Questionnaire> findByTenantIdAndId(String tenantId, String id);
}
