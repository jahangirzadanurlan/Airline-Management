package com.example.userms.repository;

import com.example.userms.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query(value = "select * from _user where username = :username or email = :username",nativeQuery = true)
    Optional<User> findUserByUsernameOrEmail(String username);

    Optional<User> findUserById(Long id);
}
