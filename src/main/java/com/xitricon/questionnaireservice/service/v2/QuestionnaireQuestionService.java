package com.xitricon.questionnaireservice.service.v2;

import com.xitricon.questionnaireservice.dto.v2.QuestionnaireInputDTO;
import com.xitricon.questionnaireservice.dto.v2.QuestionnaireOutputDTO;
import com.xitricon.questionnaireservice.dto.v2.QuestionnaireQuestionUpdateInputDTO;
import com.xitricon.questionnaireservice.dto.v2.QuestionnaireUpdateInputDTO;

public interface QuestionnaireQuestionService {
	QuestionnaireOutputDTO getQuestionaireById(String tenantId, String id);

	QuestionnaireOutputDTO createQuestionnaire(QuestionnaireInputDTO questionnaireInput);

	QuestionnaireOutputDTO updateQuestionnaire(String tenantId, String id, QuestionnaireUpdateInputDTO questionnaireUpdateInput);

	QuestionnaireOutputDTO updateQuestions(String tenantId, String id,
			QuestionnaireQuestionUpdateInputDTO questionnaireQuestionUpdateInput);
}