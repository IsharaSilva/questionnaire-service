package com.xitricon.questionnaireservice.model.v2;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xitricon.questionnaireservice.dto.v2.QuestionnaireQuestionOutputDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class QuestionnaireQuestion {

	@Builder.Default
	private ObjectId id = new ObjectId();

	private String tenantId;
	private String questionRef;
	private String dependsOn;
	private String determinator;

	@JsonIgnore
	public QuestionnaireQuestionOutputDTO viewAsDTO() {
		return new QuestionnaireQuestionOutputDTO(this.id.toString(), this.tenantId, this.questionRef, this.dependsOn,
				this.determinator);
	}

}
