package com.xitricon.questionnaireservice.dto.v2;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter

public class QuestionnaireInputDTO {
	private final String tenantId;
	private final String title;
	private final List<QuestionnaireQuestionInputDTO> questions;

	@JsonCreator
	public QuestionnaireInputDTO(@JsonProperty("tenantId") final String tenantId,
			@JsonProperty("title") final String title,
			@JsonProperty("questions") final List<QuestionnaireQuestionInputDTO> questions) {
		this.tenantId = tenantId;
		this.title = title;
		this.questions = questions;
	}

}
