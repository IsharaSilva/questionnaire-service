package com.xitricon.questionnaireservice.dto;

import com.xitricon.questionnaireservice.model.enums.QuestionType;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class SingleAnswerOutputDTO extends QuestionServiceOutputDTO {

	public SingleAnswerOutputDTO(String id, String tenantId, String title, QuestionType type,
			List<Map<String, String>> validations) {
		super(id, tenantId, title, type, validations);
	}

}