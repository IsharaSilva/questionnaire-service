package com.xitricon.questionnaireservice.dto.v2;

import lombok.Getter;

@Getter
public class QuestionnaireQuestionOutputDTO extends QuestionnaireQuestionInputDTO {
	private final String id;

	public QuestionnaireQuestionOutputDTO(final String id, final String tenantId, final String questionRef,
			final String dependsOn, final String determinator) {
		super(tenantId, questionRef, dependsOn, determinator);
		this.id = id;
	}
}
