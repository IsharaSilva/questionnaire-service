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
			TypeReference<Questionnaire> typeReference = new TypeReference<Questionnaire>() {
			};

			InputStream inputStream = TypeReference.class.getResourceAsStream("/questionnaire-data.json");

			try {
				Questionnaire questionnaireToSave = mapper.readValue(inputStream, typeReference);

				List<Questionnaire> existingQuestionnaire = questionnaireRepository.findAll();

				if (!existingQuestionnaire.isEmpty()) {
					log.info("Questionnaire data exists");
					return;
				}

				questionnaireRepository.save(questionnaireToSave);

				log.info("Questionnaire Saved");
			} catch (IOException e) {
				log.error("Unable to save questionnaire");
			}
		};
	}

}
