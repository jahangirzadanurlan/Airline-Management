FROM adoptopenjdk/openjdk11
EXPOSE 8085
ARG JAR_FILE=build/libs/flight-ms-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} flight.jar
ENTRYPOINT ["java", "-jar","/flight.jar"]