package com.xitricon.questionnaireservice.dto.v2;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class QuestionnaireQuestionUpdateInputDTO {
	private final List<String> removals;
	private final List<String> additions;

	@JsonCreator
	public QuestionnaireQuestionUpdateInputDTO(@JsonProperty("removals") final List<String> removals,
			@JsonProperty("additions") final List<String> additions) {
		this.removals = removals;
		this.additions = additions;
	}
}
