package com.xitricon.questionnaireservice.service.v2;

import org.springframework.stereotype.Service;

import com.xitricon.questionnaireservice.common.exception.ResourceNotFoundException;
import com.xitricon.questionnaireservice.dto.v2.QuestionnaireQuestionInputDTO;
import com.xitricon.questionnaireservice.dto.v2.QuestionnaireQuestionOutputDTO;
import com.xitricon.questionnaireservice.model.v2.QuestionnaireQuestion;
import com.xitricon.questionnaireservice.repository.v2.QuestionnaireQuestionRepository;

@Service
public class QuestionnaireQuestionServiceImpl implements QuestionnaireQuestionService {

	private final QuestionnaireQuestionRepository questionnaireQuestionRepository;

	public QuestionnaireQuestionServiceImpl(final QuestionnaireQuestionRepository questionnaireQuestionRepository) {
		this.questionnaireQuestionRepository= questionnaireQuestionRepository;
	}

	@Override
    public QuestionnaireQuestionOutputDTO createQuestionnaire() {
        QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
        QuestionnaireQuestion savedQuestionnaire = questionnaireQuestionRepository.save(questionnaireQuestion);
        return savedQuestionnaire.viewAsDTO();
    }
	@Override
	public QuestionnaireQuestionOutputDTO updateQuestionnaire(QuestionnaireQuestionInputDTO questionnaireQuestionInputDTO) {
		QuestionnaireQuestion questionnaireQuestion = questionnaireQuestionRepository.findById(questionnaireQuestionInputDTO.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Questionnaire not found"));
		
		QuestionnaireQuestion updateQuestionnaire = questionnaireQuestionRepository.save(questionnaireQuestion);
		return updateQuestionnaire.viewAsDTO();
	}

	@Override
	public QuestionnaireQuestionOutputDTO addQuestion(String id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'addQuestion'");
	}

	@Override
	public QuestionnaireQuestionOutputDTO removeQuestion(String id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'removeQuestion'");
	}


	@Override
	public QuestionnaireQuestionOutputDTO getQuestionaireQuestionById(String id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getQuestionaireQuestionById'");
	}

	

}
