package com.fbreaper.schedulers;

import com.fbreaper.service.PostDataToStorageAggregatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true", matchIfMissing = false)
public class PostDataToFirebaseAggregatorScheduler {

    @Autowired
    PostDataToStorageAggregatorService postDataToFirebaseAggregator;

    @Value("${scheduling.fixed.delay.ms}")
    long fixedDelayMs;

    private Logger log = LoggerFactory.getLogger(PostDataToFirebaseAggregatorScheduler.class);

    @Scheduled(fixedDelayString = "${scheduling.fixed.delay.ms}")
    public void schedulerPostDataToFirebaseAggregator(){
        log.info("Starting scheduled run (fixedDelay: {}ms)...", fixedDelayMs);
        postDataToFirebaseAggregator.aggregatePostData();
    }

}
