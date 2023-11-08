package com.xitricon.questionnaireservice.controller.v2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xitricon.questionnaireservice.dto.v2.QuestionnaireQuestionInputDTO;
import com.xitricon.questionnaireservice.dto.v2.QuestionnaireQuestionOutputDTO;
import com.xitricon.questionnaireservice.service.v2.QuestionnaireQuestionService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/v2/questionnaires")
public class QuestionnaireQuestionController {

	private final QuestionnaireQuestionService questionnaireQuestionService;

	public QuestionnaireQuestionController(final QuestionnaireQuestionService questionnaireQuestionService) {
		this.questionnaireQuestionService= questionnaireQuestionService;
	}

	@PostMapping("/createQuestionnaire")
	public ResponseEntity<QuestionnaireQuestionOutputDTO> createQuestionnaire() {
		QuestionnaireQuestionOutputDTO createQuestionnaire = questionnaireQuestionService.createQuestionnaire();
		return ResponseEntity.ok(createQuestionnaire);
	}

	@PutMapping("/{id}")
	public ResponseEntity<QuestionnaireQuestionOutputDTO> updateQuestionnaire(@RequestBody QuestionnaireQuestionInputDTO questionnaireQuestionInputDTO) {
		QuestionnaireQuestionOutputDTO updateQuestionnaire = questionnaireQuestionService.updateQuestionnaire(questionnaireQuestionInputDTO);
		return ResponseEntity.ok(updateQuestionnaire);
	}

}