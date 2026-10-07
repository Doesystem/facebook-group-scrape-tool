package com.fbreaper.fbuiapi.pages;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

@PropertySource(value = "classpath:application.properties")
public abstract class AbstractPage {

    private Logger log = LoggerFactory.getLogger(AbstractPage.class);

    @Value("${selenide.timeout}")
    long timeout;

    @Value("${browser.headless:false}")
    boolean headless;

    @PostConstruct
    private void applySelenideConfig(){
        Configuration.timeout = timeout;
        Configuration.browser = BrowserType.FIREFOX;
        Configuration.headless = headless;
        Configuration.savePageSource = false;
        Configuration.screenshots = false;
        Configuration.browserSize = "1920x1080";

        // Use pre-installed geckodriver if available (Docker/Linux)
        String geckodriverPath = System.getenv("WEBDRIVER_GECKO_DRIVER");
        if (geckodriverPath != null && !geckodriverPath.isEmpty()) {
            log.info("Using pre-installed geckodriver: {}", geckodriverPath);
            System.setProperty("webdriver.gecko.driver", geckodriverPath);
        }

        if (headless) {
            log.info("Configuring Firefox headless mode...");
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("moz:firefoxOptions", options);
            Configuration.browserCapabilities = caps;
        }
    }

    public void waitABit(Integer mls){
        try{
            Thread.sleep(mls);
        }catch (Throwable t){
            log.warn("IGNORED Throwable [" + t + "]");
        }
    }

}
