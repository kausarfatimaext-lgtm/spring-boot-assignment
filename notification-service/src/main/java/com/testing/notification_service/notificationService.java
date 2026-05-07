package com.testing.notification_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class notificationService {
    private notificationRepo repository;
    private notificationEntity notification;

    public notificationService(notificationRepo repository){
        this.repository = repository;
    }

    public notificationEntity createNotification(notificationEntity notification){
        try {
            log.info("Creating notification");
            this.notification = new notificationEntity();
            this.notification = notification;
            repository.save(this.notification);
            log.info("notification is added successfully ");
        } catch (Exception e){
            log.info("On creating notification following error is occured: {}",e.getMessage());
        }
        return this.notification;
    }
}
