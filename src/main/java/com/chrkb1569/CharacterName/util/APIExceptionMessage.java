package com.chrkb1569.CharacterName.util;

import java.util.Arrays;
import java.util.Optional;

public enum APIExceptionMessage {
    ERROR_MESSAGE_1("OPENAPI_CONTENT_0001", "서버 내부에서 오류가 발생하였습니다."),
    ERROR_MESSAGE_2("OPENAPI_CONTENT_0002", "사용자 권한이 유효하지 않습니다."),
    ERROR_MESSAGE_3("OPENAPI_CONTENT_0003", "식별자를 다시 한 번 확인해주세요."),
    ERROR_MESSAGE_4("OPENAPI_CONTENT_0004", "입력하신 파라미터의 값이 유효하지 않습니다."),
    ERROR_MESSAGE_5("OPENAPI_CONTENT_0005", "API Key를 확인해주세요."),
    ERROR_MESSAGE_6("OPENAPI_CONTENT_0006", "요청 URL을 확인해주세요."),
    ERROR_MESSAGE_7("OPENAPI_CONTENT_0007", "금일 API 호출 횟수를 초과하였습니다."),
    ERROR_MESSAGE_9("OPENAPI_CONTENT_0009", "데이터 준비 중입니다."),
    ERROR_MESSAGE_10("OPENAPI_CONTENT_0010", "서비스 점검 중입니다.")
    ;

    private final static String DEFAULT_ERROR_MESSAGE = "API 요청에 실패하였습니다.";
    private String errorType;
    private String errorMessage;

    APIExceptionMessage(String errorType, String errorMessage) {
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }

    public static String getErrorMessageByType(String errorType) {
        Optional<APIExceptionMessage> exceptionMessage = Arrays.stream(values())
                .filter(message -> message.errorType.equals(errorType))
                .findFirst();

        if(!exceptionMessage.isPresent()) return DEFAULT_ERROR_MESSAGE;

        return exceptionMessage.get().errorMessage;
    }
}