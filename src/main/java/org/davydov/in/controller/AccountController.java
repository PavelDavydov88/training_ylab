package org.davydov.in.controller;

import lombok.RequiredArgsConstructor;
import org.davydov.model.ResponseDTO;
import org.davydov.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Контроллер для предоставления счета игрока
 */
@RestController
@RequiredArgsConstructor
public class AccountController {

    private final PlayerService playerService;

    /**
     * Метод для предоставления счета игрока
     *
     * @param headers заголовок с токеном
     * @return значение счета
     */
    @PostMapping("/account/{idPlayer}")
    public ResponseEntity<?> account(@PathVariable Long idPlayer, @RequestHeader Map<String, String> headers) {
        String token = headers.get("token");
        try {
            if (token == null) {
                throw new RuntimeException("token is null");
            }
            Long accountPlayer = playerService.getAccount(idPlayer, token);
            return new ResponseEntity<>(new ResponseDTO(String.valueOf(accountPlayer)), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
