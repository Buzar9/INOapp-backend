# Krok 1: Używamy lekkiego obrazu bazowego z JRE
FROM openjdk:21-jdk-slim

# Krok 2: Instalujemy pakiet gdal-bin
# Używamy `\` do łączenia poleceń w jednej linii, co jest standardową praktyką
RUN apt-get update && \
    apt-get install -y gdal-bin && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Krok 3: Określamy argument JAR_FILE
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# Krok 4: Uruchamiamy aplikację Spring Boot
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]