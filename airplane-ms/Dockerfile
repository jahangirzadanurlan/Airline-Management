FROM adoptopenjdk/openjdk11
EXPOSE 8083
ARG JAR_FILE=build/libs/airplane-ms-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} airplane.jar
ENTRYPOINT ["java", "-jar","/airplane.jar"]