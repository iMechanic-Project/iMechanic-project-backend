FROM amazoncorretto:21-alpine-jdk

COPY target/iMechanic-project-backend-0.0.1-SNAPSHOT.jar iMechanic-app.jar

ENTRYPOINT ["java", "-jar", "iMechanic-app.jar"]