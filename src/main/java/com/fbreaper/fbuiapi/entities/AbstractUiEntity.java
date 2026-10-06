package com.fbreaper.fbuiapi.entities;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import com.fbreaper.fbuiapi.pages.AbstractPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class AbstractUiEntity extends AbstractPage {

    private Logger log = LoggerFactory.getLogger(AbstractUiEntity.class);

    protected static final String NO_DATA_FLAG = "NO DATA";
    protected static final String DATA_UTIME_ATTRIBUTE = "data-utime";
    protected static final String HREF_ATTRIBUTE = "href";

    public static String SCROLL_OPTION = "{behavior: \"instant\", block: \"end\", inline: \"end\"}";

    protected String fetchTextData(SelenideElement element){
        try {
            String value = element.getText();
            if (value == null || value.trim().isEmpty()) {
                log.warn("fetchTextData: element found but text is empty. Element: {}", element);
            }
            return value;
        } catch (ElementNotFound e){
            log.warn("fetchTextData: ElementNotFound for element: {}", element);
            return NO_DATA_FLAG;
        } catch (Throwable t){
            log.warn("fetchTextData: Unexpected error [{}] for element: {}", t.getMessage(), element);
            return NO_DATA_FLAG;
        }
    }

    protected String fetchAttributeData(SelenideElement element, String attributeName){
        try {
            String value = element.getAttribute(attributeName);
            if (value == null || value.trim().isEmpty()) {
                log.warn("fetchAttributeData: attribute '{}' is null/empty. Element: {}", attributeName, element);
            }
            return value;
        } catch (ElementNotFound e){
            log.warn("fetchAttributeData: ElementNotFound for attribute '{}'. Element: {}", attributeName, element);
            return NO_DATA_FLAG;
        } catch (Throwable t){
            log.warn("fetchAttributeData: Unexpected error [{}] for attribute '{}'. Element: {}", t.getMessage(), attributeName, element);
            return NO_DATA_FLAG;
        }
    }

    protected String fetchInnerTextData(SelenideElement element){
        try {
            String value = element.innerText();
            if (value == null || value.trim().isEmpty()) {
                log.warn("fetchInnerTextData: element found but innerText is empty. Element: {}", element);
            }
            return value;
        } catch (ElementNotFound e){
            log.warn("fetchInnerTextData: ElementNotFound for element: {}", element);
            return NO_DATA_FLAG;
        } catch (Throwable t){
            log.warn("fetchInnerTextData: Unexpected error [{}] for element: {}", t.getMessage(), element);
            return NO_DATA_FLAG;
        }
    }

}
