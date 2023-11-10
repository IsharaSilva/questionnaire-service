package com.xitricon.questionnaireservice.dto.v2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class QuestionnaireQuestionInputDTO {
	private final String questionRef;
	private final String dependsOn;
	private final String determinator;

	@JsonCreator
	public QuestionnaireQuestionInputDTO(@JsonProperty("questionRef") final String questionRef,
			@JsonProperty("dependsOn") final String dependsOn, @JsonProperty("determinator") final String determinator) {
		this.questionRef = questionRef;
		this.dependsOn = dependsOn;
		this.determinator = determinator;
	}
	
}
