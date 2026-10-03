package com.fbreaper.utils.aggregators.configs;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@PropertySource(value = "classpath:properties/PostDataToFirebase.properties")
@Component
public class PostDataToFirebaseAggregatorConfig {

	@Value("${fb.login}")
	private String fbLogin;
	@Value("${fb.pass}")
	private String fbPass;
	@Value("${fb.group.url}")
	private String fbGroupUrl;
	@Value("${posts.to.fetch}")
	private Integer postsToFetch;

	@Override
	public String toString() {
		return "PostDataToFirebaseAggregatorConfig{" + ", fbLogin='" + fbLogin + '\'' + ", fbPass='" + fbPass + '\''
				+ ", fbGroupUrl='" + fbGroupUrl + '\'' + ", postsToFetch=" + postsToFetch + '}';
	}

	public String getFbLogin() {
		return fbLogin;
	}

	public void setFbLogin(String fbLogin) {
		this.fbLogin = fbLogin;
	}

	public String getFbPass() {
		return fbPass;
	}

	public void setFbPass(String fbPass) {
		this.fbPass = fbPass;
	}

	public String getFbGroupUrl() {
		return fbGroupUrl;
	}

	public void setFbGroupUrl(String fbGroupUrl) {
		this.fbGroupUrl = fbGroupUrl;
	}

	public Integer getPostsToFetch() {
		return postsToFetch;
	}

	public void setPostsToFetch(Integer postsToFetch) {
		this.postsToFetch = postsToFetch;
	}

}

