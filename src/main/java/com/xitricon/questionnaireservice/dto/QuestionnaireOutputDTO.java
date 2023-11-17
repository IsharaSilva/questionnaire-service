package com.xitricon.questionnaireservice.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xitricon.questionnaireservice.util.CommonConstants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
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
	private final List<QuestionnairePageOutputDTO> pages;
}
