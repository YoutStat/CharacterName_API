package com.chrkb1569.CharacterName.service;

import com.chrkb1569.CharacterName.exception.APIResultException;
import com.chrkb1569.CharacterName.util.APIExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
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

    @Value("${api.info.limitLevel}")
    private int LIMIT_LEVEL;

    private final APIService apiService;

    public List<String> getCharacterNames(final int pageNumber) {
        String apiData = apiService.getAPIData(pageNumber);

        checkValidation(apiData);

        return parseData(apiData);
    }

    private void checkValidation(String apiData) {
        JSONObject jsonObject = new JSONObject(apiData);

        if(!jsonObject.has(ERROR_KEY)) return;

        JSONObject errorObject = (JSONObject)jsonObject.get(ERROR_KEY);
        String errorMessage = APIExceptionMessage.getErrorMessageByType((String)errorObject.get(ERROR_TYPE));

        throw new APIResultException(errorMessage);
    }

    private List<String> parseData(String apiData) {
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
}