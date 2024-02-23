package com.chrkb1569.CharacterName.controller;

import com.chrkb1569.CharacterName.service.CharacterNameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/characterNames")
    @ResponseStatus(HttpStatus.OK)
    public void saveCharacterNames() {
        characterNameService.saveCharacterName();
    }
}