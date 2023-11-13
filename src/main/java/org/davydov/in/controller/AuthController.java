package org.davydov.in.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.davydov.model.PlayerDTO;
import org.davydov.model.ResponseDTO;
import org.davydov.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Контроллер для предоставления auth
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Метод для предоставления auth
     *
     * @param idPlayer DI игрока
     * @param dto      DTO игрока
     * @return токен
     */
    @PostMapping("/auth/{idPlayer}")
    public ResponseEntity<?> auth(@PathVariable Long idPlayer, @RequestBody @Valid PlayerDTO dto) {
        try {
            Optional<String> token = authService.doAuthorization(idPlayer, dto);
            return new ResponseEntity<>(new ResponseDTO(token.orElse("default token")), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
