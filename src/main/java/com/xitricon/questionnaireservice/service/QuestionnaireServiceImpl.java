package com.xitricon.questionnaireservice.service;

import org.springframework.stereotype.Service;

import com.xitricon.questionnaireservice.common.exception.ResourceNotFoundException;
import com.xitricon.questionnaireservice.dto.QuestionnaireOutputDTO;
import com.xitricon.questionnaireservice.model.Questionnaire;
import com.xitricon.questionnaireservice.repository.QuestionnaireRepository;

@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {

	private final QuestionnaireRepository questionnaireRepository;

	public QuestionnaireServiceImpl(final QuestionnaireRepository questionnaireRepository) {
		this.questionnaireRepository = questionnaireRepository;
	}

	@Override
	public QuestionnaireOutputDTO getQuestionairesById(String id) {
		return questionnaireRepository.findById(id).map(Questionnaire::viewAsDTO)
				.orElseThrow(() -> new ResourceNotFoundException("Questionnaire not found"));
	}

}
