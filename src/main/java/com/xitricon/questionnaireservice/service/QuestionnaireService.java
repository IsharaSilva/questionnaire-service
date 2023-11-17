package com.xitricon.questionnaireservice.service;

import com.xitricon.questionnaireservice.dto.QuestionnaireOutputDTO;

public interface QuestionnaireService {
	QuestionnaireOutputDTO getQuestionairesById(String id);

    QuestionnaireOutputDTO addQuestionToQuestionnaire(String questionnaireId, String questionId, String pageId);
}