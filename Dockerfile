## Stage 1: Build JAR
FROM maven:3.6-openjdk-8 AS builder

WORKDIR /build
COPY pom.xml .
# Download dependencies first (cached if pom.xml unchanged)
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn package -DskipTests -q

## Stage 2: Runtime (Java 8 on Ubuntu 20.04)
FROM eclipse-temurin:8-jre-focal

# Install Firefox ESR and dependencies
RUN apt-get update && apt-get install -y \
    wget \
    bzip2 \
    ca-certificates \
    firefox \
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

# Download geckodriver for linux64 (amd64) explicitly
RUN GECKODRIVER_VERSION=0.35.0 && \
    wget -q "https://github.com/mozilla/geckodriver/releases/download/v${GECKODRIVER_VERSION}/geckodriver-v${GECKODRIVER_VERSION}-linux64.tar.gz" \
    -O /tmp/geckodriver.tar.gz && \
    tar -xzf /tmp/geckodriver.tar.gz -C /usr/local/bin/ && \
    chmod +x /usr/local/bin/geckodriver && \
    rm /tmp/geckodriver.tar.gz

WORKDIR /app

COPY --from=builder /build/target/fbreaper-*.jar app.jar
COPY src/main/resources/application.properties application.properties
COPY src/main/resources/properties/PostDataToFirebase.properties PostDataToFirebase.properties

# Tell WebDriverManager to use the pre-installed geckodriver
ENV WEBDRIVER_GECKO_DRIVER=/usr/local/bin/geckodriver

ENTRYPOINT ["java", \
    "-jar", "app.jar", \
    "--spring.config.location=file:application.properties", \
    "--spring.config.additional-location=file:PostDataToFirebase.properties"]
