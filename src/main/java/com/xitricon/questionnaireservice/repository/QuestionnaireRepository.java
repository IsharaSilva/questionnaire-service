package com.xitricon.questionnaireservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.xitricon.questionnaireservice.model.Questionnaire;

import java.util.Optional;

public interface QuestionnaireRepository extends MongoRepository<Questionnaire, String> {

	Optional<Questionnaire> findByTenantIdAndId(String TenantId, String id);
}
