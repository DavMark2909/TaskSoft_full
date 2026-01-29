package com.tasksoft.mark.mainservice.repository;

import com.tasksoft.mark.mainservice.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
