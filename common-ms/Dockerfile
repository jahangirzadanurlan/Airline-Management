FROM adoptopenjdk/openjdk11
EXPOSE 8080
ARG JAR_FILE=build/libs/common-ms-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} common-ms.jar
ENTRYPOINT ["java", "-jar","/common-ms.jar"]