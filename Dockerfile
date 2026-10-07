FROM eclipse-temurin:8-jre

# Install Firefox and dependencies
RUN apt-get update && apt-get install -y \
    wget \
    bzip2 \
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

# Download Firefox ESR directly from Mozilla
RUN wget -q "https://download.mozilla.org/?product=firefox-esr-latest&os=linux64&lang=en-US" \
    -O /tmp/firefox.tar.bz2 \
    && tar -xjf /tmp/firefox.tar.bz2 -C /opt/ \
    && ln -s /opt/firefox/firefox /usr/local/bin/firefox \
    && rm /tmp/firefox.tar.bz2

# geckodriver will be auto-downloaded by WebDriverManager at runtime
WORKDIR /app

COPY target/fbreaper-*.jar app.jar
COPY src/main/resources/application.properties application.properties
COPY src/main/resources/properties/PostDataToFirebase.properties PostDataToFirebase.properties

ENTRYPOINT ["java", \
    "-jar", "app.jar", \
    "--spring.config.location=file:application.properties", \
    "--spring.config.additional-location=file:PostDataToFirebase.properties"]
