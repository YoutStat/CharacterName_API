package com.chrkb1569.CharacterName.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.chrkb1569.CharacterName.util.APIValidator.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParseService {
    @Value("${api.request.name.rankingKey}")
    private String RANKING_KEY;

    @Value("${api.request.name.nameKey}")
    private String NAME_KEY;

    @Value("${api.request.name.levelKey}")
    private String LEVEL_KEY;

    @Value("${api.request.name.limitLevel}")
    private int LIMIT_LEVEL;

    @Value("${api.info.identifierKey}")
    private String IDENTIFIER_KEY;

    private final APIService apiService;

    public List<String> getCharacterNames(final long pageNumber) {
        String apiData = apiService.getCharacterNames(pageNumber);

        if(!checkAPIValidation(apiData)) return null;

        return parseDataToCharacterNames(apiData);
    }

    public String getCharacterIdentifier(final String characterName) {
        String apiData = apiService.getCharacterIdentifier(characterName);

        if(!checkAPIValidation(apiData)) return null;

        return parseDataToIdentifier(apiData);
    }

    private List<String> parseDataToCharacterNames(String apiData) {
        List<String> characterNames = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(apiData);
        JSONArray ranking = jsonObject.getJSONArray(RANKING_KEY);

        for(Object object : ranking) {
            jsonObject = new JSONObject(object.toString());
            int level = (int)jsonObject.get(LEVEL_KEY);

            if(level < LIMIT_LEVEL) break;

            characterNames.add((String)jsonObject.get(NAME_KEY));
        }

        return characterNames;
    }

    private String parseDataToIdentifier(String apiData) {
        JSONObject jsonObject = new JSONObject(apiData);

        return jsonObject.getString(IDENTIFIER_KEY);
    }
}