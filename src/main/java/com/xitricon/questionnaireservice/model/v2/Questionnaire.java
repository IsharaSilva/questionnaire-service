package com.xitricon.questionnaireservice.model.v2;

import java.util.List;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xitricon.questionnaireservice.audit.Auditable;
import com.xitricon.questionnaireservice.dto.v2.QuestionnaireOutputDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@Audited
@Document(collection = "questionnaires_v2")
public class Questionnaire extends Auditable<String> {

	@Id
	private String id;
	private String title;
	private List<QuestionnaireQuestion> questions;

	@JsonIgnore
	public QuestionnaireOutputDTO viewAsDTO() {
		return new QuestionnaireOutputDTO(this.id, this.title, this.createdAt, this.modifiedAt, this.createdBy,
				this.modifiedBy, this.questions.stream().map(QuestionnaireQuestion::viewAsDTO).toList());
	}
}