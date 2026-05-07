package com.testing.notification_service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
@Component
public class Scheduler {
    notificationRepo repository;

    public Scheduler(notificationRepo repository){
        this.repository = repository;
    }

    @Transactional
    @Scheduled(fixedDelay = 60000)
    public void scheduleTask() {
        try  {
            List<notificationEntity> notifications = (List<notificationEntity>) repository.findAll();
            if (notifications.isEmpty()) {
                return;
            }
            notifications.forEach(notification -> {
                log.info("Here is the notification info save in db: {}", notification.getMessage());
            });
            repository.deleteAll();
        } catch (Exception ex) {
            log.error("Error occurred while scheduling task {}", ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
