package com.xitricon.questionnaireservice.dto;

import com.xitricon.questionnaireservice.model.enums.QuestionType;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class DocumentOutputDTO extends QuestionServiceOutputDTO {
    private final List<String> fileType;

    public DocumentOutputDTO(String id, String title, QuestionType type, List<Map<String, String>> validations, List<String> fileType) {
        super(id, title, type, validations);
        this.fileType = fileType;
    }


}
