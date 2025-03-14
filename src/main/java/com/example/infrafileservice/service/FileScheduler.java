package com.example.infrafileservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileScheduler {
    @Scheduled(cron = "${spring.task.scheduling.temp}")
    public void changeToDelete() {
        log.info("changeToDelete");
    }

    @Scheduled(cron = "${spring.task.scheduling.delete}")
    public void deleteFile() {
        log.info("deleteFile");
    }
}
