package com.commerce.repository;

import com.commerce.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<user, String> {
    Optional<user> findByKakao(String kakao);
    Optional<user> findAllByKakao(String kakao);


}