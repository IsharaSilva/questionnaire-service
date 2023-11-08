package com.xitricon.questionnaireservice.model.v2;

import org.bson.types.ObjectId;
import org.hibernate.envers.Audited;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xitricon.questionnaireservice.dto.v2.QuestionnaireQuestionOutputDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@Audited
public class QuestionnaireQuestion  {

   	@Builder.Default
	private ObjectId id = new ObjectId();
    private String title;
    private String questionRef;
    private String dependsOn;
    private String determinator;
    private List<QuestionnaireQuestion> questions;//

    @JsonIgnore
    public QuestionnaireQuestionOutputDTO viewAsDTO() {
        return new QuestionnaireQuestionOutputDTO(this.id.toString(), this.title,  this.questionRef, this.dependsOn, this.determinator,
            this.questions.stream().map(QuestionnaireQuestion::viewAsDTO).collect(Collectors.toList()));
    }
    
}
