package com.closememo.query.infra.persistence.readmodel.notification;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationReadModelRepository extends
    JpaRepository<NotificationReadModel, String> {

}
