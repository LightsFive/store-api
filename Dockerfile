FROM amazoncorretto:17
LABEL authors="krishna"

RUN mkdir "app"
WORKDIR app
COPY target/store-api-0.0.1-SNAPSHOT.jar .
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "store-api-0.0.1-SNAPSHOT.jar"]