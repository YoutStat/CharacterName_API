package com.chrkb1569.CharacterName.controller;

import com.chrkb1569.CharacterName.service.CharacterNameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CharacterNameController {
    private final CharacterNameService characterNameService;

    @GetMapping("/characterNames")
    @ResponseStatus(HttpStatus.OK)
    public void getCharacterNames() {
        characterNameService.getCharacterNamesByAPI();
    }
}