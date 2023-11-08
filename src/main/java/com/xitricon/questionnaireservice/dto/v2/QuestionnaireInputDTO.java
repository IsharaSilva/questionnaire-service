package com.xitricon.questionnaireservice.dto.v2;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuestionnaireInputDTO {
	private final String title;
	private final List<QuestionnaireQuestionInputDTO> questions;
}
