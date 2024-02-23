package com.chrkb1569.CharacterName.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.bucketName}")
    private String bucketName;

    private final static String TEXT_FILE_EXTENSION = ".txt";
    private final static String SEPERATOR = ", ";

    public void saveCharacterNames(String fileName, List<String> characterNames) {
        String addedExtension = addExtension(fileName);

        byte[] byteArr = String.join(SEPERATOR, characterNames).getBytes(StandardCharsets.UTF_8);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("text/plain; charset=utf-8");
        metadata.setContentEncoding("UTF-8");

        amazonS3Client.putObject(bucketName, addedExtension, new ByteArrayInputStream(byteArr), metadata);
    }

    public List<String> getCharacterNames(String fileName) {
        String fileKey = addExtension(fileName);

        S3Object characterName = amazonS3Client.getObject(bucketName, fileKey);

        String convertedValue = convertObjectToString(characterName);

        return Arrays.stream(convertedValue.split(SEPERATOR)).toList();
    }

    private String addExtension(String fileName) {
        return fileName + TEXT_FILE_EXTENSION;
    }

    private String convertObjectToString(S3Object characterName) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(characterName.getObjectContent(), "UTF-8"));
            StringBuilder response = new StringBuilder();

            while (true) {
                String inputValue = br.readLine();

                if(inputValue == null) break;

                response.append(inputValue);
            }

            br.close();

            return response.toString();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}