package com.chrkb1569.CharacterName.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.chrkb1569.CharacterName.util.APIExceptionMessage.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParseService {
    @Value("${api.info.errorKey}")
    private String ERROR_KEY;

    @Value("${api.info.errorType}")
    private String ERROR_TYPE;

    @Value("${api.info.rankingKey}")
    private String RANKING_KEY;

    @Value("${api.info.nameKey}")
    private String NAME_KEY;

    @Value("${api.info.levelKey}")
    private String LEVEL_KEY;

    @Value("${api.info.identifierKey}")
    private String IDENTIFIER_KEY;

    @Value("${api.info.limitLevel}")
    private int LIMIT_LEVEL;

    private final APIService apiService;

    public List<String> getCharacterNames(final int pageNumber) {
        String apiData = apiService.getCharacterNames(pageNumber);

        checkValidation(apiData);

        return parseDataToCharacterNames(apiData);
    }

    public String getCharacterIdentifier(final String characterName) {
        String apiData = apiService.getCharacterIdentifier(characterName);

        if(!checkValidation(apiData)) return null;

        return parseDataToIdentifier(apiData);
    }

    private boolean checkValidation(String apiData) {
        JSONObject jsonObject = new JSONObject(apiData);

        if(!jsonObject.has(ERROR_KEY)) return true;

        JSONObject errorObject = (JSONObject)jsonObject.get(ERROR_KEY);
        String errorMessage = getErrorMessageByType((String)errorObject.get(ERROR_TYPE));

        log.info(errorMessage);

        return false;
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