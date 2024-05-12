FROM openjdk:17-oracle
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} carServiceManagement-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/carServiceManagement-0.0.1-SNAPSHOT.jar"]
