package org.davydov.in.controller;

import lombok.RequiredArgsConstructor;
import org.davydov.model.AccountOperationDTO;
import org.davydov.model.ResponseDTO;
import org.davydov.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Контроллер для выполнения дебита игрока
 */
@RestController
@RequiredArgsConstructor
public class DebitController {

    private final PlayerService playerService;

    /**
     * Метод для выполнения дебита игрока
     *
     * @param idPlayer ID игрока
     * @param headers заголовок с токеном
     * @param dto     DTO операции со счетом
     * @return счет игрока
     */
    @PostMapping("/debit/{idPlayer}")
    public ResponseEntity<?> debit(@PathVariable Long idPlayer, @RequestBody AccountOperationDTO dto, @RequestHeader Map<String, String> headers) {
        String token = headers.get("token");
        try {
            if (token == null) {
                throw new RuntimeException("token is null");
            }
            Long accountPlayer = playerService.debitAccount(idPlayer, dto, token);
            return new ResponseEntity<>(new ResponseDTO(String.valueOf(accountPlayer)), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
