package com.xitricon.questionnaireservice.dto.v2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuestionnaireQuestionInputDTO {
	private final String questionRef;
	private final String dependsOn;
	private final String determinator;
}
