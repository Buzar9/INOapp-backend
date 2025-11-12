FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

COPY gradlew ./
COPY gradle/ ./gradle/

RUN chmod +x ./gradlew

COPY build.gradle.kts settings.gradle.kts ./

COPY src/ ./src/

RUN ./gradlew bootJar -x test


FROM eclipse-temurin:21-jre-jammy

RUN apt-get update && \
    apt-get install -y --no-install-recommends gdal-bin && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]