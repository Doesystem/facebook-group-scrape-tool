package com.fbreaper.utils.aggregators.configs;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@PropertySource(value = "classpath:properties/PostDataToFirebase.properties")
@Component
public class PostDataToFirebaseAggregatorConfig {

	@Value("${fb.login}")
	private String fbLogin;

	@Value("${fb.pass}")
	private String fbPass;

	@Value("#{'${fb.group.ids}'.split(',')}")
	private List<String> fbGroupIds;

	@Value("${posts.to.fetch}")
	private Integer postsToFetch;

	@Override
	public String toString() {
		return "PostDataToFirebaseAggregatorConfig{" +
				"fbLogin='" + fbLogin + '\'' +
				", fbPass='" + fbPass + '\'' +
				", fbGroupIds=" + fbGroupIds +
				", postsToFetch=" + postsToFetch + '}';
	}

	public String getFbLogin() { return fbLogin; }
	public void setFbLogin(String fbLogin) { this.fbLogin = fbLogin; }

	public String getFbPass() { return fbPass; }
	public void setFbPass(String fbPass) { this.fbPass = fbPass; }

	public List<String> getFbGroupIds() { return fbGroupIds; }
	public void setFbGroupIds(List<String> fbGroupIds) { this.fbGroupIds = fbGroupIds; }

	public Integer getPostsToFetch() { return postsToFetch; }
	public void setPostsToFetch(Integer postsToFetch) { this.postsToFetch = postsToFetch; }

}
