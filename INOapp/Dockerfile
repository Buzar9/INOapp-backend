# Etap buildowania
FROM openjdk:17-jdk-slim AS build
WORKDIR /app

# Skopiuj pliki konfiguracyjne osobno, by cache był używany
COPY INOapp/settings.gradle.kts INOapp/build.gradle.kts ./
COPY INOapp/gradlew ./gradlew
COPY INOapp/gradle ./gradle

# Ustaw uprawnienia do wrappera
RUN chmod +x ./gradlew

# Skopiuj resztę projektu
COPY INOapp/src ./src

# Buduj aplikację
RUN ./gradlew bootJar --no-daemon

# Etap uruchamiania
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
