package com.spring.boot.application.services.notification;

import com.spring.boot.application.common.enums.NotificationFilter;
import com.spring.boot.application.common.utils.RestAPIStatus;
import com.spring.boot.application.common.utils.UniqueID;
import com.spring.boot.application.common.utils.Validator;
import com.spring.boot.application.controller.model.response.company.NotificationResponse;
import com.spring.boot.application.entity.Job;
import com.spring.boot.application.entity.Notification;
import com.spring.boot.application.entity.User;
import com.spring.boot.application.repositories.JobRepository;
import com.spring.boot.application.repositories.NotificationRepository;
import com.spring.boot.application.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService{
    final private UserRepository userRepository;
    final private JobRepository jobRepository;
    final private NotificationRepository notificationRepository;

    public NotificationServiceImpl(UserRepository userRepository,
                                   JobRepository jobRepository,
                                   NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification addNotification(String userId, String jobId, String content) {
        User u = userRepository.getById(userId);
        Validator.notNull(u, RestAPIStatus.NOT_FOUND, "");

        Job j = jobRepository.getById(jobId);
        Validator.notNull(j, RestAPIStatus.NOT_FOUND, "");

        Notification notification = new Notification();
        notification.setId(UniqueID.getUUID());
        notification.setUserId(u.getId());
        notification.setJobId(j.getId());
        notification.setRead(false);
        notification.setCompanyId(j.getCompanyId());
        notification.setContent(content);
        return notificationRepository.save(notification);
    }

    @Override
    public Notification markAsRead(String id) {
        Notification notification = notificationRepository.getById(id);
        Validator.notNull(notification, RestAPIStatus.NOT_FOUND, "");

        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> markAllAsRead(String companyId) {
        List<Notification> notifications = notificationRepository.getAllByCompanyIdAndRead(companyId, false);

        for (Notification notification : notifications) {
            notification.setRead(true);
        }

        return notificationRepository.saveAll(notifications);
    }

    @Override
    public Page<NotificationResponse> getAllNotificationByCompanyId(String companyId, NotificationFilter filter,
                                                            int pageNumber, int pageSize) {
        PageRequest request = PageRequest.of(pageNumber - 1, pageSize);
        if (filter.equals(NotificationFilter.ALL)) {
            return notificationRepository.getAllByCompanyId(companyId, request);
        }

        boolean read = false;

        read = filter.equals(NotificationFilter.READ);
        return notificationRepository.getAllByCompanyId(companyId, read, request);
    }
}
