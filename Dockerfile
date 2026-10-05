FROM eclipse-temurin:8-jre

# Install Firefox and dependencies
RUN apt-get update && apt-get install -y \
    firefox-esr \
    libgtk-3-0 \
    libdbus-glib-1-2 \
    libxt6 \
    && rm -rf /var/lib/apt/lists/*

# geckodriver will be auto-downloaded by WebDriverManager at runtime
WORKDIR /app

COPY target/fbreaper-*.jar app.jar
COPY src/main/resources/application.properties application.properties
COPY src/main/resources/properties/PostDataToFirebase.properties PostDataToFirebase.properties

ENTRYPOINT ["java", \
    "-jar", "app.jar", \
    "--spring.config.location=file:application.properties", \
    "--spring.config.additional-location=file:PostDataToFirebase.properties"]
