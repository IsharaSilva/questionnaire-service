package com.xitricon.questionnaireservice.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xitricon.questionnaireservice.dto.QuestionnairePageOutputDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class QuestionnairePage {

	@Builder.Default
	private ObjectId id = new ObjectId();

	private int index;
	private String title;
	private List<Question> questions;

	@JsonIgnore
	public QuestionnairePageOutputDTO viewAsDTO() {
		return new QuestionnairePageOutputDTO(this.id.toString(), this.index, this.title,

				(Objects.nonNull(questions) ? this.questions.stream().map(Question::viewAsDTO).toList()
						: Collections.emptyList())

		);
	}
}
