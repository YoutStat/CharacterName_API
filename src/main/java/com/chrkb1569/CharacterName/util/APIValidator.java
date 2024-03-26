package com.chrkb1569.CharacterName.util;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import static com.chrkb1569.CharacterName.util.APIExceptionMessage.getErrorMessageByType;

@Slf4j
public class APIValidator {
    private static final String ERROR_KEY = "error";
    private static final String ERROR_TYPE = "name";

    // API 데이터 유효성 확인
    public static boolean checkAPIValidation(String apiData) {
        JSONObject jsonObject = new JSONObject(apiData);

        if(!jsonObject.has(ERROR_KEY)) return true;

        JSONObject errorObject = (JSONObject)jsonObject.get(ERROR_KEY);
        String errorMessage = getErrorMessageByType((String)errorObject.get(ERROR_TYPE));

        log.info(errorMessage);

        return false;
    }
}