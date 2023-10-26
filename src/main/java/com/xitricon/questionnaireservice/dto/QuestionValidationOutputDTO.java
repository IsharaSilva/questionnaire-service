package com.xitricon.questionnaireservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuestionValidationOutputDTO {
	private final boolean required;
}
