package com.testing.notification_service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface notificationRepo extends CrudRepository<notificationEntity, Integer> {
}
