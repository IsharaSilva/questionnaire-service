package com.xitricon.questionnaireservice.dto.v2;

import java.util.List;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuestionnaireQuestionInputDTO {
    private final String title;
    private final String questionRef;
    private final String dependsOn;
    private final String determinator;
    private final List<QuestionnaireQuestionOutputDTO> questions; //TODO, convert to inputDTO //
    
}
