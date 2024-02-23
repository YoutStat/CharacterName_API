package com.chrkb1569.CharacterName.repository;

import com.chrkb1569.CharacterName.domain.CharacterName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterNameRepository extends JpaRepository<CharacterName, Long> {
}