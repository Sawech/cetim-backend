# Use OpenJDK 17 as base image
FROM eclipse-temurin:17-jdk-alpine AS build

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Fix permissions for mvnw
RUN chmod +x ./mvnw

# Download dependencies (this layer will be cached)
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build the application (skip tests for faster builds)
RUN ./mvnw clean package -DskipTests

# Use a smaller JRE image for runtime
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Copy the WAR file from build stage (will be labs-0.0.1-SNAPSHOT.war)
COPY --from=build /app/target/*.war /app/app.war

# Create uploads directory for file storage
RUN mkdir -p /app/uploads

# Expose port
EXPOSE 8080

# Run the WAR file as a standalone Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.war"]