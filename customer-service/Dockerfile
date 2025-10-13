FROM openjdk:21-jdk
WORKDIR /app
COPY target/customer-service-0.0.1-SNAPSHOT.jar /app/customer.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/customer.jar"]