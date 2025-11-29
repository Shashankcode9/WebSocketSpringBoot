FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .
RUN ./mvnw dependency:go-offline

COPY src ./src
RUN ./mvnw package -DskipTests

EXPOSE 8080
CMD ["java", "-Xms256m", "-Xmx350m", "-jar", "/app/target/WebSocketSpringBoot-0.0.1-SNAPSHOT.jar"]
