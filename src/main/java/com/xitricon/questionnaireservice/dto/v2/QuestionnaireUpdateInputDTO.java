package com.xitricon.questionnaireservice.dto.v2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class QuestionnaireUpdateInputDTO {
	private final String title;

	@JsonCreator
	public QuestionnaireUpdateInputDTO(@JsonProperty("title") final String title) {
		this.title = title;
	}
}
