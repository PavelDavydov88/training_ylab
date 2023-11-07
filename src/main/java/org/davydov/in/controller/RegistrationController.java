package org.davydov.in.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.davydov.model.PlayerDTO;
import org.davydov.model.ResponseDTO;
import org.davydov.model.ResponseError;
import org.davydov.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для регистрации игрока
 */
@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final PlayerService playerService;

    /**
     * Метод для регистрации игрока
     *
     * @param dto DTO игрока
     * @return уведомление о регистрации игрока
     */
    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody @Valid PlayerDTO dto) {
        try {
            long idPlayer = playerService.create(dto);
            return new ResponseEntity<>(new ResponseDTO(idPlayer, ""), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseError(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
