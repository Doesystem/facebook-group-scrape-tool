package com.fbreaper.service;

import com.codeborne.selenide.WebDriverRunner;
import com.fbreaper.domain.PostDto;
import com.fbreaper.fbuiapi.services.FbUiInteractionService;
import com.fbreaper.repository.DaoUtils;
import com.fbreaper.repository.ExcelWriterService;
import com.fbreaper.repository.rdms.PostgreSqlPostRepository;
import com.fbreaper.utils.aggregators.configs.PostDataToFirebaseAggregatorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource(value = "classpath:properties/PostDataToFirebase.properties")
public class PostDataToStorageAggregatorService {

    private Logger log = LoggerFactory.getLogger(PostDataToStorageAggregatorService.class);

    @Autowired
    FbUiInteractionService fb;

    @Autowired
    PostDataToFirebaseAggregatorConfig config;

    @Autowired
    ExcelWriterService excelService;

    @Autowired
    PostgreSqlPostRepository postgreSqlRepository;

    @Value("${export.to}")
    private String export;

    private boolean excel = false;
    private boolean postgres = false;


    @PostConstruct
    public void initProp() {
        if(export!=null && !export.isEmpty()) {
            String output[] = export.split("&");
            for(String o : output){
                if(o.trim().equalsIgnoreCase("excel")) excel = true;
                if(o.trim().equalsIgnoreCase("postgres")) postgres = true;
            }
        } else {
            log.error("Output is not specified at 'export' parameter");
        }
    }

    public void aggregatePostData(){
        log.info("Starting aggregate data...");
        try {

            List<PostDto> posts = new ArrayList<>();
            fb
                .setGroupUrl(config.getFbGroupUrl())
//                .login(config.getFbLogin(), config.getFbPass())
                .goToGroup()
                .getPosts(config.getPostsToFetch())
                    .forEach(uiPost -> {
                        PostDto post = DaoUtils.doMapPostUiRepresentationTexDataToDto(uiPost);
                        log.info("doMapPostUiRepresentationTexDataToDto");
                        posts.add(post);
                    });
            if(postgres){
                log.debug("***Postgres output selected***");
                List<PostDto> newPosts = posts.stream()
                        .filter(post -> {
                            if (post.getGroupId() == null || post.getPostFbId() == null) {
                                log.warn("Skipping post with null groupId/postFbId: link=[{}]", post.getPostLink());
                                return false;
                            }
                            boolean exists = postgreSqlRepository.existsByGroupIdAndPostFbId(post.getGroupId(), post.getPostFbId());
                            if (exists) {
                                log.info("Skipping duplicate post: groupId=[{}] postFbId=[{}]", post.getGroupId(), post.getPostFbId());
                            }
                            return !exists;
                        })
                        .collect(java.util.stream.Collectors.toList());
                log.info("Saving {}/{} new posts (skipped {} duplicates)", newPosts.size(), posts.size(), posts.size() - newPosts.size());
                postgreSqlRepository.saveAll(newPosts);
            }
            if(excel) {
                log.debug("***Excel output selected***");
//                excelService.write(posts);
            }



        } finally {
            try {
                WebDriverRunner.getWebDriver().quit();
            } catch (Throwable t){
                log.warn("IGNORE ERROR [" + t + "] during browser closing...");
            }
        }
        log.info("Task execution finished. Waiting for next launch according to schedule...");
    }

}