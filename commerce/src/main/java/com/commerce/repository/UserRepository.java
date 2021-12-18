package com.commerce.repository;

import com.commerce.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<user, String> {

    Optional<user> findBySocial(String social);

    Optional<user> findAllBySocial(String social);


}