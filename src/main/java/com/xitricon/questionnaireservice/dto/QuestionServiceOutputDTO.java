package com.xitricon.questionnaireservice.dto;

import com.xitricon.questionnaireservice.model.enums.QuestionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class QuestionServiceOutputDTO {
    private final String id;
    private final String title;
    private final QuestionType type;
    private final List<Map<String,String>> validations;
}
