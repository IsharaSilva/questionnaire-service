package com.xitricon.questionnaireservice.dto.v2;

import java.util.List;

import lombok.Getter;

@Getter
public class QuestionnaireQuestionOutputDTO extends QuestionnaireQuestionInputDTO {
    private final String id;
   
    public QuestionnaireQuestionOutputDTO(final String id, final String title, final String questionRef, final String dependsOn, final String determinator, final List<QuestionnaireQuestionOutputDTO> questions){
        super(id, title, questionRef, dependsOn, determinator, questions); 
        this.id = id;

    }

  

}
