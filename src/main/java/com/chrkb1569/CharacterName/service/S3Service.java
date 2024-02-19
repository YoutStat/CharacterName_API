package com.chrkb1569.CharacterName.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.bucketName}")
    private String bucketName;

    private final static String TEXT_FILE_EXTENSION = ".txt";

    public void saveCharacterNames(String fileName, List<String> characterNames) {
        String addedExtension = addExtension(fileName);

        byte[] byteArr = String.join(", ", characterNames).getBytes(StandardCharsets.UTF_8);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("text/plain; charset=utf-8");
        metadata.setContentEncoding("UTF-8");

        amazonS3Client.putObject(bucketName, addedExtension, new ByteArrayInputStream(byteArr), metadata);
    }

    private String addExtension(String fileName) {
        return fileName + TEXT_FILE_EXTENSION;
    }
}