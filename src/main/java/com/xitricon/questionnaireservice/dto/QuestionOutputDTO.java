package com.xitricon.questionnaireservice.dto;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuestionOutputDTO {
    private final String id;
    private final int index;
    private final String label;
    private final String type;
    private final String group;
    private final List<QuestionValidationOutputDTO> validations;
    private final boolean editable;
    private final Object optionsSource;
    private final List<QuestionOutputDTO> subQuestions;
}
