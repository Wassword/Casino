# Use an official OpenJDK runtime as a parent image
FROM openjdk:22-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Add a volume to store logs
VOLUME /tmp

# Copy the JAR file from the target directory (after building)
COPY target/Casino-0.0.1-SNAPSHOT.jar /app/CasinoApp.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Command to run the app
ENTRYPOINT ["java", "-jar", "/app/CasinoApp.jar"]
