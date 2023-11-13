package com.xitricon.questionnaireservice.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Collections;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xitricon.questionnaireservice.dto.QuestionOutputDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class Question {

	@Builder.Default
	private ObjectId id = new ObjectId();

	private int index;
	private String label;
	private String type;
	private String group;
	private List<QuestionValidation> validations;
	private boolean editable;
	private Object optionsSource;
	private List<Question> subQuestions;

	@JsonIgnore
	public QuestionOutputDTO viewAsDTO() {

		List<QuestionOutputDTO> subQuestionDTOs = new ArrayList<>();

		if (this.getSubQuestions() != null) {
			for (Question subQuestion : this.getSubQuestions()) {
				subQuestionDTOs.add(subQuestion.viewAsDTO());
			}
		}

		return new QuestionOutputDTO(this.id.toString(), this.index, this.label, this.type, this.group,

				(Objects.nonNull(this.validations)
						? this.validations.stream().map(QuestionValidation::viewAsDTO).collect(Collectors.toList())
						: Collections.emptyList()),

				this.editable, this.optionsSource, subQuestionDTOs);
	}
}
