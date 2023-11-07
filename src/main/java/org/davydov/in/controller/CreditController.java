package org.davydov.in.controller;

import lombok.RequiredArgsConstructor;
import org.davydov.model.AccountDTO;
import org.davydov.model.AccountOperationDTO;
import org.davydov.model.ResponseError;
import org.davydov.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Контроллер для выполнения кредита игрока
 */
@RestController
@RequiredArgsConstructor
public class CreditController {

    private final PlayerService playerService;

    /**
     * Метод для выполнения кредита игрока
     *
     * @param headers заголовок с токеном
     * @param dto     DTO операции со счетом
     * @return счет игрока
     */
    @PostMapping("/credit")
    public ResponseEntity<?> credit(@RequestBody AccountOperationDTO dto, @RequestHeader Map<String, String> headers) {
        String token = headers.get("token");
        try {
            if (token == null) {
                throw new RuntimeException("token is null");
            }
            Long accountPlayer = playerService.creditAccount(dto, token);
            return new ResponseEntity<>(new AccountDTO(accountPlayer), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseError(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
