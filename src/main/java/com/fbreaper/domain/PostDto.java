package com.fbreaper.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Data
@Entity
@Table(name = "group_posts",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_group_post", columnNames = {"group_id", "post_id"})
    },
    indexes = {
        @Index(name = "idx_create_date", columnList = "create_date"),
        @Index(name = "idx_status", columnList = "status")
    }
)
public class PostDto implements Comparable<PostDto> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "post_id")
    private String postFbId;

    @Column(name = "post_author")
    private String postAuthor;

    @Column(name = "post_author_url", columnDefinition="TEXT")
    private String postAuthorUrl;

    @Column(name = "post_last_update")
    private Long postLastUpdate;

    @Column(name = "post_time_stamp")
    private Long postTimeStamp;

    @Column(name = "post_text", columnDefinition="TEXT")
    private String postText;

    //skip
    private String postType;

    @Column(name = "post_link", columnDefinition="TEXT")
    private String postLink;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "status", nullable = false)
    private Integer status;

    @PrePersist
    private void prePersist() {
        this.createDate = LocalDateTime.now();
        if (this.status == null) {
            this.status = 0;
        }
    }

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PostImage> images = new ArrayList<>();

    public static final String ID_FIELD = "id";
    public static final String POST_AUTHOR_FIELD = "postAuthor";
    public static final String POST_AUTHOR_URL_FIELD = "postAuthorUrl";
    public static final String POST_LAST_UPDATE_FIELD = "postLastUpdate";
    public static final String POST_TIME_STAMP_FIELD = "postTimeStamp";
    public static final String POST_TEXT_FIELD = "postText";
    public static final String POST_TYPE_FIELD = "postType";
    public static final String POST_LINK_FIELD = "postLink";
    public static final String IMAGES_FIELD = "images";


    @Override
    public String toString() {
        return  "PostEntity:\n" +
                "              ID = '" + id + "\n" +
                "     POST AUTHOR = '" + postAuthor + "'\n" +
                " POST AUTHOR URL = '" + postAuthorUrl + "'\n" +
                "  POST TIMESTAMP = '" + postLastUpdate + "'\n" +
                "POST_LAST_UPDATE = '" + postLastUpdate + "'\n"+
                "       POST TEXT = '" + postText + "'\n"+
                "       POST TYPE = '" + postType + "'\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostDto)) return false;
        PostDto that = (PostDto) o;
        return
                Objects.equals(postAuthor, that.postAuthor) &&
                Objects.equals(postAuthorUrl, that.postAuthorUrl) &&
                Objects.equals(postLastUpdate, that.postLastUpdate) &&
                Objects.equals(postTimeStamp, that.postTimeStamp) &&
                Objects.equals(postText, that.postText);
    }

    @Override
    public int compareTo(PostDto that) {
        return Comparator.<PostDto, String>comparing(p -> p.getPostAuthor())
                .thenComparing(p -> p.getPostTimeStamp())
                .compare(this, that);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postAuthor, postAuthorUrl, postLastUpdate, postTimeStamp, postText);
    }

    public Map textFieldsToHasMap(){
        return new HashMap(){{
            put(POST_AUTHOR_FIELD, postAuthor);
            put(POST_AUTHOR_URL_FIELD, postAuthorUrl);
            put(POST_LAST_UPDATE_FIELD, postLastUpdate);
            put(POST_TIME_STAMP_FIELD, postTimeStamp);
            put(POST_TEXT_FIELD, postText);
            put(POST_TYPE_FIELD, postType);
            put(POST_LINK_FIELD, postLink);
        }};
    }

    public void setPostTimeStampWithMls(String postTimeStamp) {
        this.postTimeStamp = Long.valueOf(postTimeStamp + "000");
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getPostFbId() {
		return postFbId;
	}

	public void setPostFbId(String postFbId) {
		this.postFbId = postFbId;
	}

	public String getPostAuthor() {
		return postAuthor;
	}

	public void setPostAuthor(String postAuthor) {
		this.postAuthor = postAuthor;
	}

	public String getPostAuthorUrl() {
		return postAuthorUrl;
	}

	public void setPostAuthorUrl(String postAuthorUrl) {
		this.postAuthorUrl = postAuthorUrl;
	}

	public Long getPostLastUpdate() {
		return postLastUpdate;
	}

	public void setPostLastUpdate(Long postLastUpdate) {
		this.postLastUpdate = postLastUpdate;
	}

	public Long getPostTimeStamp() {
		return postTimeStamp;
	}

	public void setPostTimeStamp(Long postTimeStamp) {
		this.postTimeStamp = postTimeStamp;
	}

	public String getPostText() {
		return postText;
	}

	public void setPostText(String postText) {
		this.postText = postText;
	}

	public String getPostType() {
		return postType;
	}

	public void setPostType(String postType) {
		this.postType = postType;
	}

	public String getPostLink() {
		return postLink;
	}

	public void setPostLink(String postLink) {
		this.postLink = postLink;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<PostImage> getImages() {
		return images;
	}

	public void setImages(List<PostImage> images) {
		this.images = images;
	}

//    /*Timstamp formated similar to facebook UI. Needs to compare.*/
//    public String getOriginPostTimeStamp(){
//        try {
//            return postTimeStamp.substring(0, 9);
//        } catch (StringIndexOutOfBoundsException e){
//            log.warn("Can't get original time stamp in POST [id= '" + this.id + "', author = '" + postAuthor + "']. Possible data duplication!");
//            return "0000000000";
//        }
//    }
    
    
}
