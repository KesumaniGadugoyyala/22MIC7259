package com.affordmed.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.affordmed.entity.Notification;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, UUID>, JpaSpecificationExecutor<Notification> {
    Optional<Notification> findByIdAndStudentId(UUID id, String studentId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Notification n set n.read = true where n.studentId = :studentId and n.read = false")
    int markAllRead(@Param("studentId") String studentId);

    @Query("select n from Notification n where n.studentId = :studentId order by n.createdAt desc")
    List<Notification> findRecentForPriority(@Param("studentId") String studentId, Pageable pageable);
}
