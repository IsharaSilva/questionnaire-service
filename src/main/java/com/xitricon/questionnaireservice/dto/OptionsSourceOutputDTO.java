package com.xitricon.questionnaireservice.dto;

import com.xitricon.questionnaireservice.util.SourceType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OptionsSourceOutputDTO {
    private final SourceType type;
    private final String key;
}
