## Stage 1: Build JAR
FROM maven:3.6-openjdk-8 AS builder

WORKDIR /build
COPY pom.xml .
# Download dependencies first (cached if pom.xml unchanged)
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn package -DskipTests -q

## Stage 2: Runtime
FROM debian:bookworm-slim

# Install Java and Firefox ESR
RUN apt-get update && apt-get install -y \
    ca-certificates \
    openjdk-17-jre-headless \
    firefox-esr \
    libgtk-3-0 \
    libdbus-glib-1-2 \
    libxt6 \
    libx11-xcb1 \
    libxcomposite1 \
    libxcursor1 \
    libxdamage1 \
    libxi6 \
    libxrandr2 \
    libxss1 \
    libxtst6 \
    libasound2 \
    fonts-liberation \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=builder /build/target/fbreaper-*.jar app.jar
COPY src/main/resources/application.properties application.properties
COPY src/main/resources/properties/PostDataToFirebase.properties PostDataToFirebase.properties

ENTRYPOINT ["java", \
    "-jar", "app.jar", \
    "--spring.config.location=file:application.properties", \
    "--spring.config.additional-location=file:PostDataToFirebase.properties"]
