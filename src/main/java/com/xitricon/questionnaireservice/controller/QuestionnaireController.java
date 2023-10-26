package com.xitricon.questionnaireservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xitricon.questionnaireservice.dto.QuestionnaireOutputDTO;
import com.xitricon.questionnaireservice.service.QuestionnaireService;

@RestController
@RequestMapping("/api/questionnaires")
public class QuestionnaireController {

	private final QuestionnaireService questionnaireService;

	public QuestionnaireController(final QuestionnaireService questionnaireService) {
		this.questionnaireService = questionnaireService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<QuestionnaireOutputDTO> getQuestionnaireById(@PathVariable String id) {
		return ResponseEntity.ok(questionnaireService.getQuestionairesById(id));
	}

}