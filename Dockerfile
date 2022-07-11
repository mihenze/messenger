FROM openjdk:11
COPY ./target/messenger-0.0.1-SNAPSHOT.jar /messenger.jar
CMD ["java", "-jar", "/messenger.jar"]

