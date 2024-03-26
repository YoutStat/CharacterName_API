package com.chrkb1569.CharacterName.config.batch;

import com.chrkb1569.CharacterName.service.ParseService;
import com.chrkb1569.CharacterName.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.chrkb1569.CharacterName.util.APIExceptionMessage.*;

@Component
@RequiredArgsConstructor
public class CharacterNameTasklet implements Tasklet {
    @Value("${api.request.name.pageKey}")
    private String PAGE_KEY;

    @Value("${api.request.name.maxListSize}")
    private int MAX_LIST_SIZE;

    @Value("${api.file.name}")
    private String BASIC_FILE_NAME;

    private final ParseService parseService;
    private final S3Service s3Service;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        long PAGE_NUMBER = getPageNumber(contribution);

        String fileName = BASIC_FILE_NAME + PAGE_NUMBER;

        try {
            List<String> characterNames = parseService.getCharacterNames(PAGE_NUMBER);
            List<String> characterIdentifier = getCharacterIdentifier(characterNames);

            s3Service.saveCharacterNames(fileName, characterIdentifier);

            if(characterNames.size() != MAX_LIST_SIZE) return RepeatStatus.FINISHED;

            contribution.getStepExecution().getJobExecution().getExecutionContext().putLong(PAGE_KEY, ++PAGE_NUMBER);
        } catch (Exception e) {
            return RepeatStatus.FINISHED;
        }

        return RepeatStatus.CONTINUABLE;
    }

    private long getPageNumber(StepContribution contribution) {
        return contribution.getStepExecution().getJobExecution().getExecutionContext().getLong(PAGE_KEY);
    }

    private List<String> getCharacterIdentifier(List<String> characterNames) throws Exception {
        List<String> characterIdentifier = new ArrayList<>();

        for(String characterName : characterNames) {
            String identifier = parseService.getCharacterIdentifier(characterName);

            if(identifier.equals(DEFAULT_ERROR_MESSAGE)) continue;

            characterIdentifier.add(identifier);
            Thread.sleep(1000);
        }

        return characterIdentifier;
    }
}