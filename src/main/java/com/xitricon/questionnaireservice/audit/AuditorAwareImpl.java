package com.xitricon.questionnaireservice.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		// use actual user or the system here
		return Optional.of("System");

	}
}
