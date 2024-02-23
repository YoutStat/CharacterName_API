package com.chrkb1569.CharacterName.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@Table(name = "CHARACTER_NAME")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CharacterName {
    @Id
    @Column(name = "character_name_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String characterName; // 캐릭터 이름

    @Column(nullable = false, updatable = false)
    private String characterIdentifier; // 캐릭터 식별자

    public CharacterName(String characterName, String characterIdentifier) {
        this.characterName = characterName;
        this.characterIdentifier = characterIdentifier;
    }
}