package com.xitricon.questionnaireservice.dto;

import java.util.List;
import java.util.Map;

import com.xitricon.questionnaireservice.util.QuestionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuestionOutputDTO {
	private final String id;
	private final int index;
	private final String label;
	private final QuestionType type;
	private final String group;
	private final List<Map<String,String>> validations;
	private final boolean editable;
	private final OptionsSourceOutputDTO optionsSource;
	private final List<QuestionOutputDTO> subQuestions;
}
