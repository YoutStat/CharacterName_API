package com.chrkb1569.CharacterName.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterNameService {
    private final S3Service s3Service;
    private final ParseService parseService;
    private static int INITIAL_PAGE = 1;
    private static final int MAXIMUM_LIST_SIZE = 200;

    @Value("${api.file.name}")
    private String BASIC_FILE_NAME;

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
}