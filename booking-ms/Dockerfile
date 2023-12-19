FROM adoptopenjdk/openjdk11
EXPOSE 8086
ARG JAR_FILE=build/libs/booking-ms-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} booking.jar
ENTRYPOINT ["java", "-jar","/booking.jar"]