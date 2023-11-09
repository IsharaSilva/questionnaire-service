package com.xitricon.questionnaireservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xitricon.questionnaireservice.dto.OptionsSourceOutputDTO;
import com.xitricon.questionnaireservice.dto.QuestionOutputDTO;
import com.xitricon.questionnaireservice.dto.QuestionServiceOutputDTO;
import com.xitricon.questionnaireservice.model.Question;
import com.xitricon.questionnaireservice.model.QuestionnairePage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

import com.xitricon.questionnaireservice.common.exception.ResourceNotFoundException;
import com.xitricon.questionnaireservice.dto.QuestionnaireOutputDTO;
import com.xitricon.questionnaireservice.model.Questionnaire;
import com.xitricon.questionnaireservice.repository.QuestionnaireRepository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {

	private final QuestionnaireRepository questionnaireRepository;
	private final RestTemplate restTemplate;
	private final String questionServiceUrl;
	private final ObjectMapper objectMapper;

	public QuestionnaireServiceImpl(final QuestionnaireRepository questionnaireRepository, final RestTemplateBuilder restTemplateBuilder,
									@Value("${external-api.question-service.find-by-id}") final String questionServiceUrl,
									final ObjectMapper objectMapper) {
		this.questionnaireRepository = questionnaireRepository;
		this.restTemplate = restTemplateBuilder.build();
		this.questionServiceUrl = questionServiceUrl;
		this.objectMapper = objectMapper;
	}

	@Override
	public QuestionnaireOutputDTO getQuestionairesById(String id) {
		return questionnaireRepository.findById(id).map(Questionnaire::viewAsDTO)
				.orElseThrow(() -> new ResourceNotFoundException("Questionnaire not found"));
	}

	@Override
	public QuestionnaireOutputDTO addQuestionToQuestionnaire(String questionnaireId, String questionId, String pageId) {
		Questionnaire questionnaire = questionnaireRepository.findById(questionnaireId)
				.orElseThrow(() -> new ResourceNotFoundException("Questionnaire not found"));
		log.info(String.format("Questionnaire with id: %s retrieved successfully", questionnaireId));

		QuestionServiceOutputDTO questionServiceOutputDTO = restTemplate.getForObject(questionServiceUrl + questionId, QuestionServiceOutputDTO.class);
		log.info(String.format("Question with id: %s retrieved successfully", questionId));

		List<QuestionnairePage> pages = questionnaire.getPages();
		QuestionnairePage page = pages.stream()
				.filter(questionnairePage -> pageId.equals(questionnairePage.getId().toString()))
				.findFirst().orElseThrow(() -> new ResourceNotFoundException("Page not found"));
		log.info(String.format("Page with id: %s found within questionnaire: %s", pageId, questionnaireId));

		int pageIdx = pages.indexOf(page);
		List<Question> questions = page.getQuestions();
		int questionIdxToBeSaved = !questions.isEmpty() ? questions.get(questions.size() - 1).getIndex() + 1 : 0;

		QuestionOutputDTO questionOutput = createQuestionOutput(questionServiceOutputDTO, questionIdxToBeSaved);

		Question questionEntity = convertToQuestion(questionOutput);
		questions.add(questionEntity);

		QuestionnairePage pageToBeSaved = buildPage(page, questions);
		pages.set(pageIdx, pageToBeSaved);

		Questionnaire questionnaireToBeSaved = buildQuestionnaire(questionnaire, pages);

		return questionnaireRepository.save(questionnaireToBeSaved).viewAsDTO();
	}

	private Questionnaire buildQuestionnaire(Questionnaire questionnaire, List<QuestionnairePage> pages) {
		return Questionnaire.builder()
				.id(questionnaire.getId())
				.title(questionnaire.getTitle())
				.pages(pages)
				.createdBy(questionnaire.getCreatedBy())
				.createdAt(questionnaire.getCreatedAt())
				.build();
	}

	private QuestionnairePage buildPage(QuestionnairePage page, List<Question> questions) {
		return QuestionnairePage.builder()
				.id(page.getId())
				.questions(questions)
				.title(page.getTitle())
				.index(page.getIndex())
				.build();
	}

	private QuestionOutputDTO createQuestionOutput(QuestionServiceOutputDTO questionServiceOutputDTO, int questionIdxToBeSaved) {
		return new QuestionOutputDTO(
				questionServiceOutputDTO.getId(),
				questionIdxToBeSaved,
				questionServiceOutputDTO.getTitle(),
				questionServiceOutputDTO.getType(),
				"",
				questionServiceOutputDTO.getValidations(),
				true,
				new OptionsSourceOutputDTO(null, null),
				new ArrayList<>()
		);
	}

	private Question convertToQuestion(QuestionOutputDTO questionOutputDTO) {
		try {
			return objectMapper.convertValue(questionOutputDTO, Question.class);
		} catch (IllegalArgumentException e) {
			log.error(e.getLocalizedMessage());
			return null;
		}
	}
}
