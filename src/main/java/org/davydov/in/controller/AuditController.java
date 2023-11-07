package org.davydov.in.controller;

import lombok.RequiredArgsConstructor;
import org.davydov.model.ResponseDTO;
import org.davydov.model.ResponseListDTO;
import org.davydov.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Контроллер для предоставления аудита игрока
 */
@RestController
@RequiredArgsConstructor
public class AuditController {

    private final PlayerService playerService;

    /**
     * Метод для предоставления аудита игрока
     *
     * @param headers заголовок с токеном
     * @return лист аудита игрока
     */
    @PostMapping("/audit/{idPlayer}")
    public ResponseEntity<?> audit(@PathVariable long idPlayer, @RequestHeader Map<String, String> headers) {
        String token = headers.get("token");
        try {
            if (token == null) {
                throw new RuntimeException("token is null");
            }
            List<String> listAudit = playerService.getListAuditAction(idPlayer, token);
            return new ResponseEntity<>(new ResponseListDTO(listAudit), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
