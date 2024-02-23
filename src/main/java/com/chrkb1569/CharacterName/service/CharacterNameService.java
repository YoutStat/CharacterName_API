package com.chrkb1569.CharacterName.service;

import com.chrkb1569.CharacterName.domain.CharacterName;
import com.chrkb1569.CharacterName.repository.CharacterNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterNameService {
    private final S3Service s3Service;
    private final ParseService parseService;
    private final CharacterNameRepository characterNameRepository;
    private static int INITIAL_PAGE = 1;
    private static final int MAXIMUM_LIST_SIZE = 200;

    @Value("${api.file.name}")
    private String BASIC_FILE_NAME;

    @Value("${api.info.startNumber}")
    private int startNumber;

    @Value("${api.info.endNumber}")
    private int endNumber;

    @Transactional
    public void saveCharacterName() {
        for(int number = startNumber; number <= endNumber; number++) {
            String fileName = BASIC_FILE_NAME + number;

            List<String> characterNames = s3Service.getCharacterNames(fileName);

            saveData(characterNames);
        }
    }

    public void getCharacterNamesByAPI() {
        while(true) {
            String fileName = BASIC_FILE_NAME + INITIAL_PAGE;
            List<String> characterNames = parseService.getCharacterNames(INITIAL_PAGE++);

            s3Service.saveCharacterNames(fileName, characterNames);

            if(characterNames.size() != MAXIMUM_LIST_SIZE) break;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void saveData(List<String> characterNames) {
        for(String characterName : characterNames) {
            String characterIdentifier = parseService.getCharacterIdentifier(characterName);

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
}