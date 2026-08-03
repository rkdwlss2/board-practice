package com.example.boardpractice.repository;

import com.example.boardpractice.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {

    Optional<Users> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE email = :email AND delete_date IS NOT NULL", nativeQuery = true)
    Optional<Users> findDeletedByEmail(@Param("email") String email);
}
