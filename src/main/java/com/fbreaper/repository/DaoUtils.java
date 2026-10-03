package com.fbreaper.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fbreaper.domain.PostDto;
import com.fbreaper.fbuiapi.entities.PostUiRepresentation;

public class DaoUtils {
	
	private static Logger log = LoggerFactory.getLogger(DaoUtils.class);

    public static PostDto doMapPostUiRepresentationTexDataToDto(PostUiRepresentation uiEntity){
        PostDto dto = new PostDto();
        dto.setPostAuthor(uiEntity.fetchPostAuthor());
        dto.setPostText(uiEntity.fetchPostMessageWithNoDuplicationAndFilter());
//        dto.setPostTimeStampWithMls(uiEntity.fetchPostTimestamp());
        dto.setPostAuthorUrl(cleanFbTrackingParams(uiEntity.fetchPostAuthorProfileLink()));
        dto.setPostLink(cleanFbTrackingParams(uiEntity.fetchPostLink()));
        //setPostType(dto);
        log.info("Mapped PostDto -> author: [{}], postText: [{}], link: [{}]",
                dto.getPostAuthor(), dto.getPostText(), dto.getPostLink());
        return dto;
    }

    /**
     * ตัด Facebook tracking params (__cft__, __tn__) ออกจาก URL
     * เช่น https://facebook.com/posts/123?__cft__[0]=xxx&__tn__=yyy → https://facebook.com/posts/123
     */
    private static String cleanFbTrackingParams(String url) {
        if (url == null || url.equals("NO DATA")) return url;
        int idx = url.indexOf("?__cft__");
        if (idx != -1) return url.substring(0, idx);
        idx = url.indexOf("&__cft__");
        if (idx != -1) return url.substring(0, idx);
        return url;
    }

    private static void setPostType(PostDto dto) {
        String text = dto.getPostText();
        if(contains(text, "грн")) {
            dto.setPostType("offer");
        } else {
            if (contains(text, "відгук") || contains(text, "спасибі") ||
                    contains(text,"дяку") &&
                            (!contains(text,"всім дяку") && !contains(text,"дякую всім")
                                    && !contains(text,"дякую люди"))) {
                dto.setPostType("review");
            } else {
                dto.setPostType("request");
            }
        }
    }

    private static boolean contains(String src, String segment) {
        if(src.toLowerCase().contains(segment.toLowerCase())) {
            return true;
        } else {
            return false;
        }

    }
}
