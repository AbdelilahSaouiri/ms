FROM openjdk:21-jdk
WORKDIR /app
COPY target/discovery-service-0.0.1-SNAPSHOT.jar /app/discovery.jar
EXPOSE 8761
ENTRYPOINT ["java","-jar","/app/discovery.jar"]