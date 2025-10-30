package com.johnchoi96.webservice.services;

import com.johnchoi96.webservice.entities.user.UserEntity;
import com.johnchoi96.webservice.repos.user.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepo userRepo;

    public Optional<UserEntity> getUserByEmail(final String email) {
        return userRepo.getUserByEmail(email);
    }

    @Transactional
    public boolean isActiveUser(final String email) {
        final Optional<UserEntity> optionalUserEntity = getUserByEmail(email);
        final boolean userPresent = optionalUserEntity.isPresent() && optionalUserEntity.get().isActive();
        // if user does not exist, add the record to the database for future review
        if (!userPresent) {
            addUserRecordToDatabase(email, optionalUserEntity.isPresent());
        }
        return userPresent;
    }

    private void addUserRecordToDatabase(final String email, final boolean userRecordExists) {
        log.info("Unknown email: {}, adding user record to the database.", email);
        if (userRecordExists) {
            // user exists, but is inactive. Do nothing.
            return;
        }
        final UserEntity userEntity = UserEntity.builder()
                .email(email)
                .active(false)
                .build();
        // save the new user record
        userRepo.save(userEntity);
    }
}
