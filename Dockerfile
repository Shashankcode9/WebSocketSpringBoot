# Use lightweight JDK image
FROM eclipse-temurin:17-jdk-alpine

# Create app directory
WORKDIR /app

# Copy the jar file from target folder
COPY target/*.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Run the jar with reduced memory (important for Render free tier)
ENTRYPOINT ["java", "-Xms256m", "-Xmx350m", "-jar", "app.jar"]
