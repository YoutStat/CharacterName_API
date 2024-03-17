package com.chrkb1569.CharacterName.util;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import static com.chrkb1569.CharacterName.util.APIExceptionMessage.getErrorMessageByType;

@Slf4j
public class APIValidator {
    @Value("${api.request.error.errorKey}")
    private static String ERROR_KEY;

    @Value("${api.request.error.errorType}")
    private static String ERROR_TYPE;

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