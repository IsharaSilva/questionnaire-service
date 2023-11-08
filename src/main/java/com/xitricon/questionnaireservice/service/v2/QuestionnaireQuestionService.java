package com.xitricon.questionnaireservice.service.v2;

import com.xitricon.questionnaireservice.dto.v2.QuestionnaireQuestionInputDTO;
import com.xitricon.questionnaireservice.dto.v2.QuestionnaireQuestionOutputDTO;

public interface QuestionnaireQuestionService {
	QuestionnaireQuestionOutputDTO getQuestionaireQuestionById(String id);
	QuestionnaireQuestionOutputDTO createQuestionnaire();
	QuestionnaireQuestionOutputDTO updateQuestionnaire(QuestionnaireQuestionInputDTO questionInputDTO);
	QuestionnaireQuestionOutputDTO addQuestion(String id);
	QuestionnaireQuestionOutputDTO removeQuestion(String id);
}