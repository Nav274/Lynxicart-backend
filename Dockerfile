# Use an official OpenJDK runtime as the base image 
FROM openjdk:21-slim

# Set the working directory inside the container 
WORKDIR /app 

# Copy the JAR file from your host machine to the container 
COPY target/*.jar Lynxi_cart.jar
 
# Copy the application.properties file to the container 
COPY src/main/resources/application.properties /app/config/application.properties 

# Expose the port your application container runs on 
EXPOSE 8086

# Command to run the application 
ENTRYPOINT ["java", "-jar", "Lynxi_cart.jar", "--spring.config.location=file:/app/config/application.properties"]