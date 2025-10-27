package com.johnchoi96.webservice.repos.user;

import com.johnchoi96.webservice.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Long> {

    @Query(value = """
            select user from UserEntity user
            where user.email = :EMAIL
            """)
    Optional<UserEntity> getUserByEmail(@Param("EMAIL") final String email);
}
