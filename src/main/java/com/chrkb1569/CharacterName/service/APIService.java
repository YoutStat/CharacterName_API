package com.chrkb1569.CharacterName.service;

import com.chrkb1569.CharacterName.exception.APIRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;

@Service
public class APIService {
    private final String API_KEY; // API를 호출하기 위한 Key값
    private final String HTTP_REQUEST_NAME_URL; // API를 요청하기 위한 URL
    private final String HTTP_REQUEST_IDENTIFIER_URL; // API를 요청하기 위한 URL
    private final String HTTP_REQUEST_DATE; // API 요청시에 사용되는 날짜
    private final String HTTP_REQUEST_METHOD; // API 요청시에 사용되는 HTTP Method
    private final String HTTP_REQUEST_HEADER; // API 요청시에 사용되는 HTTP 헤더값
    private final static int DATE_GAP = 1;

    public APIService(
            @Value("${api.request.key}") String key,
            @Value("${api.request.name.url}") String requestNameUrl,
            @Value("${api.request.identifier.url}") String requestIdentifierUrl,
            @Value("${api.info.requestMethod}") String requestMethod,
            @Value("${api.info.requestHeader}") String requestHeader
    ) {
        this.API_KEY = key;
        this.HTTP_REQUEST_NAME_URL = requestNameUrl;
        this.HTTP_REQUEST_IDENTIFIER_URL = requestIdentifierUrl;
        this.HTTP_REQUEST_DATE = LocalDate.now().minusDays(DATE_GAP).toString();
        this.HTTP_REQUEST_METHOD = requestMethod;
        this.HTTP_REQUEST_HEADER = requestHeader;
    }

    public String getCharacterNames(final int pageNumber) {
        try {
            HttpURLConnection connection = getURLConnection(pageNumber);

            return getAPIResult(connection);
        } catch (IOException e) {
            throw new APIRequestException();
        }
    }

    public String getCharacterIdentifier(final String characterName) {
        try {
            String encodedName = encodeCharacterName(characterName);
            HttpURLConnection connection = getURLConnection(encodedName);

            return getAPIResult(connection);
        } catch (IOException e) {
            throw new APIRequestException();
        }
    }

    private String encodeCharacterName(String characterName) throws UnsupportedEncodingException {
        return URLEncoder.encode(characterName, "UTF-8");
    }

    private HttpURLConnection getURLConnection(final int pageNumber) throws IOException {
        URL url = getURL(pageNumber);

        return getUrlConnection(url);
    }

    private HttpURLConnection getURLConnection(final String characterName) throws IOException {
        URL url = getURL(characterName);

        return getUrlConnection(url);
    }

    private URL getURL(final int pageNumber) throws IOException {
        return new URL(String.format(HTTP_REQUEST_NAME_URL, HTTP_REQUEST_DATE, pageNumber));
    }

    private URL getURL(final String characterName) throws IOException {
        return new URL(String.format(HTTP_REQUEST_IDENTIFIER_URL, characterName));
    }

    // HTTP connection 설정
    private HttpURLConnection getUrlConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod(HTTP_REQUEST_METHOD);
        connection.setRequestProperty(HTTP_REQUEST_HEADER, API_KEY);

        return connection;
    }

    private String getAPIResult(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();

        if(responseCode == HttpStatus.OK.value())
            return getResponseData(new BufferedReader(new InputStreamReader(connection.getInputStream())));
        return getResponseData(new BufferedReader(new InputStreamReader(connection.getErrorStream())));
    }

    // API 응답 데이터 파싱
    private String getResponseData(BufferedReader br) throws IOException {
        StringBuilder response = new StringBuilder();

        while (true) {
            String inputValue = br.readLine();

            if(inputValue == null) break;

            response.append(inputValue);
        }

        br.close();

        return response.toString();
    }
}