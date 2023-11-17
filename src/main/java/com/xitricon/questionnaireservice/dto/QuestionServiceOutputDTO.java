package com.xitricon.questionnaireservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xitricon.questionnaireservice.model.enums.QuestionType;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class QuestionServiceOutputDTO {
	private final String id;
	private final String title;
	private final QuestionType type;
	private final List<Map<String, String>> validations;

	public QuestionServiceOutputDTO(@JsonProperty("id") String id, @JsonProperty("title") String title,
			@JsonProperty("type") QuestionType type,
			@JsonProperty("validations") List<Map<String, String>> validations) {
		this.id = id;
		this.title = title;
		this.type = type;
		this.validations = validations;
	}
}
