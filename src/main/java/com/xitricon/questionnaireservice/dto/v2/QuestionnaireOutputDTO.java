package com.xitricon.questionnaireservice.dto.v2;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xitricon.questionnaireservice.util.CommonConstants;

import lombok.Getter;

@Getter
public class QuestionnaireOutputDTO {
	private final String id;
	private final String tenantId;
	private final String title;

	@JsonFormat(pattern = CommonConstants.DATE_TIME_FORMAT)
	private final LocalDateTime createdAt;

	@JsonFormat(pattern = CommonConstants.DATE_TIME_FORMAT)
	private final LocalDateTime modifiedAt;

	private final String createdBy;
	private final String modifiedBy;
	private final List<QuestionnaireQuestionOutputDTO> questions;

	@JsonCreator
	public QuestionnaireOutputDTO(@JsonProperty("id") final String id, @JsonProperty("tenantId") final String tenantId,
			@JsonProperty("title") final String title, @JsonProperty("createdAt") final LocalDateTime createdAt,
			@JsonProperty("modifiedAt") final LocalDateTime modifiedAt,
			@JsonProperty("createdBy") final String createdBy, @JsonProperty("modifiedBy") final String modifiedBy,
			@JsonProperty("questions") final List<QuestionnaireQuestionOutputDTO> questions) {
		this.id = id;
		this.tenantId = tenantId;
		this.title = title;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.questions = questions;
	}
}
