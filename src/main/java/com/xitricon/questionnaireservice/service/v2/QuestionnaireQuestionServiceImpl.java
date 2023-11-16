package com.xitricon.questionnaireservice.service.v2;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.xitricon.questionnaireservice.common.exception.ResourceNotFoundException;
import com.xitricon.questionnaireservice.dto.v2.QuestionnaireInputDTO;
import com.xitricon.questionnaireservice.dto.v2.QuestionnaireOutputDTO;
import com.xitricon.questionnaireservice.dto.v2.QuestionnaireQuestionUpdateInputDTO;
import com.xitricon.questionnaireservice.dto.v2.QuestionnaireUpdateInputDTO;
import com.xitricon.questionnaireservice.model.v2.Questionnaire;
import com.xitricon.questionnaireservice.model.v2.QuestionnaireQuestion;
import com.xitricon.questionnaireservice.repository.v2.QuestionnaireV2Repository;

@Service
public class QuestionnaireQuestionServiceImpl implements QuestionnaireQuestionService {

	private final QuestionnaireV2Repository questionnaireRepository;

	public QuestionnaireQuestionServiceImpl(final QuestionnaireV2Repository questionnaireRepository) {
		this.questionnaireRepository = questionnaireRepository;
	}

	@Override
	public QuestionnaireOutputDTO getQuestionaireById(String tenantId, String id) {
		return this.findByTenantIdAndId(tenantId, id).viewAsDTO();
	}

	@Override
	public QuestionnaireOutputDTO createQuestionnaire(QuestionnaireInputDTO questionnaireInput) {
		List<QuestionnaireQuestion> questionnaireQuestions = questionnaireInput.getQuestions().stream()
				.map(qi -> QuestionnaireQuestion.builder().tenantId(qi.getTenantId()).dependsOn(qi.getDependsOn())
						.determinator(qi.getDeterminator()).questionRef(qi.getQuestionRef()).build())
				.collect(Collectors.toList());

		Questionnaire questionnaireToSave = Questionnaire.builder().tenantId(questionnaireInput.getTenantId()).questions(questionnaireQuestions)
				.title(questionnaireInput.getTitle()).build();
		return this.questionnaireRepository.save(questionnaireToSave).viewAsDTO();
	}

	@Override
	public QuestionnaireOutputDTO updateQuestionnaire(String tenantId, String id, QuestionnaireUpdateInputDTO questionnaireUpdateInput) {
		Questionnaire existingQuestionnaire = this.findByTenantIdAndId(tenantId, id);

		Questionnaire questionnaireToUpdate = Questionnaire.builder().createdAt(existingQuestionnaire.getCreatedAt())
				.createdBy(existingQuestionnaire.getCreatedBy()).tenantId(existingQuestionnaire.getTenantId()).id(existingQuestionnaire.getId())
				.questions(existingQuestionnaire.getQuestions()).title(questionnaireUpdateInput.getTitle()).build();

		return this.questionnaireRepository.save(questionnaireToUpdate).viewAsDTO();
	}

	@Override
	public QuestionnaireOutputDTO updateQuestions(String tenantId, String id,
			QuestionnaireQuestionUpdateInputDTO questionnaireQuestionUpdateInput) {
		Questionnaire existingQuestionnaire = this.findByTenantIdAndId(tenantId, id);

		List<QuestionnaireQuestion> exitingQuestions = existingQuestionnaire.getQuestions();
		List<String> removals = questionnaireQuestionUpdateInput.getRemovals();

		// removal
		exitingQuestions = exitingQuestions.stream().filter(q -> !removals.contains(q.getQuestionRef()))
				.collect(Collectors.toList());

		// additions
		List<QuestionnaireQuestion> newAdditions = questionnaireQuestionUpdateInput.getAdditions().stream()
				.map(qRef -> QuestionnaireQuestion.builder().questionRef(qRef).build()).collect(Collectors.toList());

		exitingQuestions.addAll(newAdditions);

		Questionnaire questionnaireToUpdate = Questionnaire.builder().createdAt(existingQuestionnaire.getCreatedAt())
				.createdBy(existingQuestionnaire.getCreatedBy()).tenantId(existingQuestionnaire.getTenantId())
				.id(existingQuestionnaire.getId()).questions(exitingQuestions).title(existingQuestionnaire.getTitle()).build();

		return this.questionnaireRepository.save(questionnaireToUpdate).viewAsDTO();
	}

	private Questionnaire findByTenantIdAndId(String tenantId, String id) {
		return this.questionnaireRepository.findByTenantIdAndId(tenantId, id)
				.orElseThrow(() -> new ResourceNotFoundException("No Questionnaire found for this id"));
	}

}
