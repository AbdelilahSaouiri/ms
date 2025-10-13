FROM openjdk:21-jdk
WORKDIR /app
COPY target/*.jar /app/gateway.jar
EXPOSE 8888
ENTRYPOINT ["java","-jar","/app/gateway.jar"]