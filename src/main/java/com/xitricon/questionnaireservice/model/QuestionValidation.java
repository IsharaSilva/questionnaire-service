package com.xitricon.questionnaireservice.model;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xitricon.questionnaireservice.dto.QuestionValidationOutputDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class QuestionValidation {

	@Builder.Default
	private ObjectId id = new ObjectId();

	private boolean required;

	@JsonIgnore
	public QuestionValidationOutputDTO viewAsDTO() {
		return new QuestionValidationOutputDTO(this.required);
	}
}
