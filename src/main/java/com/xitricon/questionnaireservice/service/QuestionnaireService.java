package com.xitricon.questionnaireservice.service;

import com.xitricon.questionnaireservice.dto.QuestionnaireOutputDTO;

public interface QuestionnaireService {
	QuestionnaireOutputDTO getQuestionairesById(String tenantId, String id);

	QuestionnaireOutputDTO addQuestionToQuestionnaire(String tenantId, String questionnaireId, String questionId,
			String pageId);
}