package org.davydov.in.controller;

import lombok.RequiredArgsConstructor;
import org.davydov.model.IdPlayerDTO;
import org.davydov.model.ResponseDTO;
import org.davydov.model.ResponseError;
import org.davydov.model.ResponseListDTO;
import org.davydov.service.HistoryCreditDebitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Контроллер для предоставления истории действий со счетом игрока
 */
@RestController
@RequiredArgsConstructor
public class
HistoryAccountController {

    private final HistoryCreditDebitService historyCreditDebitService;

    /**
     * Метод для предоставления истории действий со счетом игрока
     *
     * @param headers заголовок с токеном
     * @return лист истории действий со счетом игрока
     */
    @PostMapping("/history")
    public ResponseEntity<?> history(@RequestBody IdPlayerDTO dto, @RequestHeader Map<String, String> headers) {
        String token = headers.get("token");
        try {
            if (token == null) {
                throw new RuntimeException("token is null");
            }
            List<String> listOperation = historyCreditDebitService.getListOperationAccount(dto.getIdPlayer(), token);
            return new ResponseEntity<>(new ResponseListDTO(listOperation), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseError(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
