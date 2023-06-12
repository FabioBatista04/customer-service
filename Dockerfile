FROM openjdk:17-oracle

WORKDIR /app


COPY build/libs/customer-service-0.0.1-SNAPSHOT.jar customer-service.jar

EXPOSE 8080

CMD ["java", "-jar", "customer-service.jar"]
