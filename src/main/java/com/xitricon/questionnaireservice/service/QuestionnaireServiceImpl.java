package com.xitricon.questionnaireservice.service;

import com.xitricon.questionnaireservice.dto.QuestionnaireOutputDTO;
import com.xitricon.questionnaireservice.dto.QuestionServiceOutputDTO;
import com.xitricon.questionnaireservice.model.Question;
import com.xitricon.questionnaireservice.model.QuestionValidation;
import com.xitricon.questionnaireservice.model.QuestionnairePage;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

import com.xitricon.questionnaireservice.common.exception.ResourceNotFoundException;
import com.xitricon.questionnaireservice.model.Questionnaire;
import com.xitricon.questionnaireservice.repository.QuestionnaireRepository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {

    private final QuestionnaireRepository questionnaireRepository;
    private final RestTemplate restTemplate;
    private final String questionServiceUrl;

    public QuestionnaireServiceImpl(final QuestionnaireRepository questionnaireRepository, final RestTemplateBuilder restTemplateBuilder,
                                    @Value("${external-api.question-service.find-by-id}") final String questionServiceUrl) {
        this.questionnaireRepository = questionnaireRepository;
        this.restTemplate = restTemplateBuilder.build();
        this.questionServiceUrl = questionServiceUrl;
    }

    @Override
    public QuestionnaireOutputDTO getQuestionairesById(String id) {
        return getQuestionnaire(id).viewAsDTO();
    }

    @Override
    public QuestionnaireOutputDTO addQuestionToQuestionnaire(String questionnaireId, String questionId, String pageId) {
        Questionnaire questionnaire = getQuestionnaire(questionnaireId);

        List<QuestionnairePage> pages = questionnaire.getPages();
        QuestionnairePage page = pages.stream()
                .filter(questionnairePage -> pageId.equals(questionnairePage.getId().toString()))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Page not found"));

        int pageIdx = pages.indexOf(page);
        List<Question> questions = page.getQuestions();
        int questionIdxToBeSaved = !questions.isEmpty() ? questions.stream().max(Comparator.comparing(Question::getIndex)).get().getIndex() + 1 : 0;

        QuestionServiceOutputDTO questionServiceOutputDTO = restTemplate.getForObject(questionServiceUrl + questionId, QuestionServiceOutputDTO.class);

        Question questionEntity = Question.builder()
                .id(new ObjectId(questionServiceOutputDTO.getId()))
                .index(questionIdxToBeSaved)
                .label(questionServiceOutputDTO.getTitle())
                .type(questionServiceOutputDTO.getType().toString())
                .group("")
                .validations(List.of(QuestionValidation.builder().required(true).build())) // TODO: Create validation output referring to service output
                .editable(true) // TODO: Determine logic for editable
                .optionsSource(null)
                .subQuestions(new ArrayList<>())
                .build();
        questions.add(questionEntity);

        QuestionnairePage pageToBeSaved = QuestionnairePage.builder()
                .id(page.getId())
                .questions(questions)
                .title(page.getTitle())
                .index(page.getIndex())
                .build();
        pages.set(pageIdx, pageToBeSaved);

        Questionnaire questionnaireToBeSaved = Questionnaire.builder()
                .id(questionnaire.getId())
                .title(questionnaire.getTitle())
                .pages(pages)
                .createdBy(questionnaire.getCreatedBy())
                .createdAt(questionnaire.getCreatedAt())
                .build();

        return questionnaireRepository.save(questionnaireToBeSaved).viewAsDTO();
    }

    private Questionnaire getQuestionnaire(String id) {
        return questionnaireRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Questionnaire not found"));
    }
}
