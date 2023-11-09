package com.xitricon.questionnaireservice.model;

import com.xitricon.questionnaireservice.dto.OptionsSourceOutputDTO;
import com.xitricon.questionnaireservice.model.enums.SourceType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class OptionsSource {
    private SourceType type;
    private String key;

    public OptionsSourceOutputDTO viewAsDTO() {
        return new OptionsSourceOutputDTO(this.type, this.key);
    }
}
