FROM openjdk:21-jdk
WORKDIR /app
COPY target/Booking-service-0.0.1-SNAPSHOT.jar /app/booking.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/booking.jar"]