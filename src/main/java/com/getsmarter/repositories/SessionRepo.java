package com.getsmarter.repositories;

import com.getsmarter.entities.Formation;
import com.getsmarter.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepo extends JpaRepository<Session, Long> {

    @Query("SELECT s FROM Session s WHERE s.created_at >= :startDate ORDER BY s.created_at DESC")
    List<Session> findRecentlyAddedSessions(LocalDateTime startDate);

}
