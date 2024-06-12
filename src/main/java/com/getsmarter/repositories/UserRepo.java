package com.getsmarter.repositories;

import com.getsmarter.entities.Session;
import com.getsmarter.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhonenumber(String phonenumber);

    void deleteByEmail(String s);

    void deleteByPhonenumber(String s);

    @Query("SELECT u FROM User u WHERE u.created_at >= :startDate ORDER BY u.created_at DESC")
    List<User> findRecentlyAddedUsers(LocalDateTime startDate);
}
