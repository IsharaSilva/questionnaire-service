package com.xitricon.questionnaireservice.dto;

import com.xitricon.questionnaireservice.model.OptionsSource;
import com.xitricon.questionnaireservice.model.enums.QuestionType;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class SelectionOutputDTO extends QuestionServiceOutputDTO {

    private final OptionsSource optionsSource;
    private final boolean multiple;

    public SelectionOutputDTO(String id, String title, QuestionType type, List<Map<String, String>> validations, OptionsSource optionsSource, boolean multiple) {
        super(id, title, type, validations);
        this.optionsSource = optionsSource;
        this.multiple = multiple;
    }

}
