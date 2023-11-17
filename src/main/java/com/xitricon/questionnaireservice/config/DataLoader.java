package com.xitricon.questionnaireservice.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xitricon.questionnaireservice.model.Questionnaire;
import com.xitricon.questionnaireservice.repository.QuestionnaireRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class DataLoader {
	@Bean
	CommandLineRunner dataInitializer(QuestionnaireRepository questionnaireRepository) {
		return args -> {
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Questionnaire>> typeReference = new TypeReference<>() {
			};

			InputStream inputStream = TypeReference.class.getResourceAsStream("/questionnaire-data.json");

			try {
				List<Questionnaire> questionnairesToSave = mapper.readValue(inputStream, typeReference);

				List<Questionnaire> existingQuestionnaire = questionnaireRepository.findAll();

				if (!existingQuestionnaire.isEmpty()) {
					log.info("Questionnaire data exists");
					return;
				}

				questionnaireRepository.saveAll(questionnairesToSave);

				log.info("Questionnaires Saved");
			} catch (IOException e) {
				log.error("Unable to save questionnaires");
			}
		};
	}

}
