FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app 

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src/main/resources/application.properties /app/config/application.properties

COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/config/application.properties /app/config/application.properties


COPY --from=build /app/target/Lynxi_cart-0.0.1-SNAPSHOT.jar .

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "Lynxi_cart-0.0.1-SNAPSHOT.jar", "--spring.config.location=file:/app/config/application.properties"]
