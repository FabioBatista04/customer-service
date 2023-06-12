FROM openjdk:17-oracle

WORKDIR /app


COPY build/libs/customer-service-1.0.jar customer-service.jar

EXPOSE 8080

CMD ["java", "-jar", "customer-service.jar"]
