package com.xitricon.questionnaireservice.model;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xitricon.questionnaireservice.audit.Auditable;
import com.xitricon.questionnaireservice.dto.QuestionnaireOutputDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@Audited
@Document(collection = "questionnaires")
public class Questionnaire extends Auditable<String> {
	@Id
	private String id;

	private String tenantId;

	private String title;

	private List<QuestionnairePage> pages;

	@JsonIgnore
	public QuestionnaireOutputDTO viewAsDTO() {
		return new QuestionnaireOutputDTO(this.id, this.tenantId, this.title, this.createdAt,
				this.modifiedAt, this.createdBy, this.modifiedBy,
				this.pages.stream().map(QuestionnairePage::viewAsDTO).collect(Collectors.toList()));
	}

}
