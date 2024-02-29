package com.example.partypurse.repositories;

import com.example.partypurse.models.JwtBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtRepository extends JpaRepository<JwtBlackList, Long> {
    Optional<JwtBlackList> findByToken(String token);
}