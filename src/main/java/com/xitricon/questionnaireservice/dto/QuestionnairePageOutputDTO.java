package com.xitricon.questionnaireservice.dto;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuestionnairePageOutputDTO {
	private final String id;
	private final int index;
	private final String title;
	private final List<QuestionOutputDTO> questions;
}
