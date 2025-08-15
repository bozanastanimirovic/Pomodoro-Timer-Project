package com.example.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.models.Team;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long>{

    Optional<Team> findByTeamName(String name);

    boolean existsByTeamName(String teamName);
}
