FROM public.ecr.aws/amazoncorretto/amazoncorretto:17

WORKDIR /app

COPY target/questionnaire-service-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]