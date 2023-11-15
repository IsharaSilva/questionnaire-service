package com.xitricon.questionnaireservice.controller.v2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import com.xitricon.questionnaireservice.dto.v2.QuestionnaireInputDTO;
import com.xitricon.questionnaireservice.dto.v2.QuestionnaireOutputDTO;
import com.xitricon.questionnaireservice.dto.v2.QuestionnaireQuestionUpdateInputDTO;
import com.xitricon.questionnaireservice.dto.v2.QuestionnaireUpdateInputDTO;
import com.xitricon.questionnaireservice.service.v2.QuestionnaireQuestionService;



@RestController
@RequestMapping("/api/v2/questionnaires")
public class QuestionnaireQuestionController {

	private final QuestionnaireQuestionService questionnaireQuestionService;

	public QuestionnaireQuestionController(final QuestionnaireQuestionService questionnaireQuestionService) {
		this.questionnaireQuestionService = questionnaireQuestionService;
	}

	@PostMapping
	public ResponseEntity<QuestionnaireOutputDTO> createQuestionnaire(
			@RequestBody QuestionnaireInputDTO questionnaireInput) {
		return new ResponseEntity<>(this.questionnaireQuestionService.createQuestionnaire(questionnaireInput),
				HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<QuestionnaireOutputDTO> updateQuestionnaire(@RequestParam String tenantId, @PathVariable String id,
			@RequestBody QuestionnaireUpdateInputDTO questionnaireQuestionInput) {
		return ResponseEntity.ok(this.questionnaireQuestionService.updateQuestionnaire(tenantId, id, questionnaireQuestionInput));
	}

	@PutMapping("/{id}/questions")
	public ResponseEntity<QuestionnaireOutputDTO> updateQuestionnaireQuestions(@RequestParam String tenantId, @PathVariable String id,
			@RequestBody QuestionnaireQuestionUpdateInputDTO questionnaireQuestionUpdateInput) {
		return ResponseEntity
				.ok(this.questionnaireQuestionService.updateQuestions(tenantId, id, questionnaireQuestionUpdateInput));
	}

	@GetMapping("/{id}")
	public ResponseEntity<QuestionnaireOutputDTO> getQuestionnaireById(@RequestParam String tenantId, @PathVariable String id) {
		return ResponseEntity.ok(this.questionnaireQuestionService.getQuestionaireById(tenantId, id));
	}

}