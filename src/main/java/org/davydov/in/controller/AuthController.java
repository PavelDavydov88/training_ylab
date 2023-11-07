package org.davydov.in.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.davydov.model.PlayerDTO;
import org.davydov.model.ResponseDTO;
import org.davydov.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * @param dto DTO игрока
     * @return токен
     */
    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody @Valid PlayerDTO dto) {
        try{
            Optional<String> token = authService.doAuthorization(dto);
            return new ResponseEntity<>(new ResponseDTO(token.orElse("default token")), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
