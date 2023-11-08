package com.xitricon.questionnaireservice.dto.v2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class QuestionnaireQuestionOutputDTO extends QuestionnaireQuestionInputDTO {
	private final String id;

	@JsonCreator
	public QuestionnaireQuestionOutputDTO(@JsonProperty("id") final String id,
			@JsonProperty("questionRef") final String questionRef, @JsonProperty("dependsOn") final String dependsOn,
			@JsonProperty("determinator") final String determinator) {
		super(questionRef, dependsOn, determinator);
		this.id = id;
	}

}
