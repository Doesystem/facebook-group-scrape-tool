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
        String cleanLink = cleanFbTrackingParams(uiEntity.fetchPostLink());
        dto.setPostLink(cleanLink);
        parseFbGroupAndPostId(dto, cleanLink);
        //setPostType(dto);
        log.info("Mapped PostDto -> author: [{}], groupId: [{}], postFbId: [{}], postText: [{}]",
                dto.getPostAuthor(), dto.getGroupId(), dto.getPostFbId(), dto.getPostText());
        return dto;
    }

    /**
     * Parse group_id และ post_id จาก Facebook URL
     * รองรับสองรูปแบบ:
     * - https://www.facebook.com/groups/1489819158035015/posts/2917388565278060/
     * - https://www.facebook.com/permalink.php?story_fbid=xxx&id=yyy
     */
    static void parseFbGroupAndPostId(PostDto dto, String url) {
        if (url == null || url.equals("NO DATA")) return;
        try {
            // รูปแบบ /groups/{groupId}/posts/{postId}/
            java.util.regex.Matcher m = java.util.regex.Pattern
                    .compile("/groups/(\\d+)/posts/(\\d+)")
                    .matcher(url);
            if (m.find()) {
                dto.setGroupId(m.group(1));
                dto.setPostFbId(m.group(2));
                return;
            }
            // รูปแบบ permalink.php?story_fbid={postId}&id={groupId}
            m = java.util.regex.Pattern
                    .compile("[?&]story_fbid=([^&]+).*[?&]id=(\\d+)")
                    .matcher(url);
            if (m.find()) {
                dto.setPostFbId(m.group(1));
                dto.setGroupId(m.group(2));
            }
        } catch (Exception e) {
            log.warn("parseFbGroupAndPostId: failed to parse url [{}]: {}", url, e.getMessage());
        }
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
