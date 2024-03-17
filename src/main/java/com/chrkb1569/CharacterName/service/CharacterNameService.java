package com.chrkb1569.CharacterName.service;

import com.chrkb1569.CharacterName.domain.CharacterName;
import com.chrkb1569.CharacterName.repository.CharacterNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterNameService {
    private final ParseService parseService;
    private final CharacterNameRepository characterNameRepository;

    private void saveData(List<String> characterNames) {
        for(String characterName : characterNames) {
            String characterIdentifier = parseService.getCharacterIdentifier(characterName);

            if(!checkValidation(characterIdentifier)) continue;

            CharacterName instance = getInstance(characterName, characterIdentifier);

            characterNameRepository.save(instance);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private CharacterName getInstance(String characterName, String characterIdentifier) {
        return new CharacterName(characterName, characterIdentifier);
    }

    private boolean checkValidation(String identifier) {
        return StringUtils.hasText(identifier);
    }
}