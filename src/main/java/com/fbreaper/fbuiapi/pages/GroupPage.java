package com.fbreaper.fbuiapi.pages;

import com.codeborne.selenide.*;
import com.fbreaper.fbuiapi.services.FbUiInteractionService;
import org.openqa.selenium.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static com.fbreaper.fbuiapi.entities.AbstractUiEntity.SCROLL_OPTION;
import static com.fbreaper.fbuiapi.entities.PostUiRepresentation.POST;

@Component
public class GroupPage extends AbstractPage {

    private String url;

    private Logger log = LoggerFactory.getLogger(GroupPage.class);

    public GroupPage(){

    }

    public GroupPage(String url){
        this.url = url;
    }

    public void open(){
        log.info("GroupPage.open() v7 - open -> sleep4s -> click Close -> sleep5s -> query");
        Selenide.open(url);
        // รอให้ popup โหลดขึ้นมาก่อน
        Selenide.sleep(4000);
        // คลิก close button ของ popup login แทน ESCAPE
        log.info("Clicking popup close button...");
        SelenideElement closeBtn = $x("//*[@aria-label='Close']");
        if (closeBtn.is(Condition.exist)) {
            closeBtn.click();
            log.info("Popup closed via close button");
        } else {
            log.warn("Close button not found, trying ESCAPE...");
            $x("//body").sendKeys(Keys.ESCAPE);
        }
        // รอหลังปิด popup ให้ Facebook render posts เพิ่ม
        Selenide.sleep(5000);
        List<SelenideElement> posts = $$x(POST);
        log.info("Posts after close+sleep: {}", posts.size());
        $$x(POST).shouldHave(CollectionCondition.sizeGreaterThan(0));
        $$x(POST).get(0).should(Condition.visible);
    }

    public List<SelenideElement> getAllPosts(){
        // snapshot หลัง sleep ใน open() แล้ว รอ stabilize อีกนิด
        Selenide.sleep(1000);
        List<SelenideElement> posts = $$x(POST);
        log.debug("getAllPosts snapshot: {} posts", posts.size());
        return posts;
    }

    public List<SelenideElement> getNewPostsBatch(Integer timeOut){
        List<SelenideElement> currentPostsBatch = getAllPosts();
        currentPostsBatch.get(currentPostsBatch.size() - 1).scrollIntoView(SCROLL_OPTION);
        return $$x(POST).shouldHave(CollectionCondition.sizeGreaterThan(currentPostsBatch.size()), timeOut);
    }

    public void setUrl(String url){
        this.url = url;
    }

}
