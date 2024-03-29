package com.xitricon.questionnaireservice.audit;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class Auditable<U> {

	@CreatedBy
	protected U createdBy;

	@CreatedDate
	protected LocalDateTime createdAt;

	@LastModifiedBy
	protected U modifiedBy;

	@LastModifiedDate
	protected LocalDateTime modifiedAt;
}
