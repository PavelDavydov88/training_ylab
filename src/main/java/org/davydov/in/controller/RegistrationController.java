package org.davydov.in.controller;

import lombok.RequiredArgsConstructor;
import org.davydov.model.PlayerDTO;
import org.davydov.model.ResponseDTO;
import org.davydov.service.PlayerService;
import org.davydov.utils.ValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final PlayerService playerService;

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody PlayerDTO dto) {
        try {
            ValidationUtils.isEmptyOrNull(dto.getName());
            ValidationUtils.size(2, 10, dto.getName());
            playerService.create(dto);
            return new ResponseEntity<>(new ResponseDTO("Player created!"), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
