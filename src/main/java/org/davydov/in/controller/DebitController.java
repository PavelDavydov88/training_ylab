package org.davydov.in.controller;

import lombok.RequiredArgsConstructor;
import org.davydov.model.AccountOperationDTO;
import org.davydov.model.ResponseDTO;
import org.davydov.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DebitController {

    private final PlayerService playerService;

    @PostMapping("/debit")
    public ResponseEntity<?> debit(@RequestHeader Map<String, String> headers, @RequestBody AccountOperationDTO dto) {
        String token = headers.get("token");
        try {
            if (token == null) {
                throw new RuntimeException("token is null");
            }
            Long accountPlayer = playerService.debitAccount(token, dto);
            return new ResponseEntity<>(new ResponseDTO(accountPlayer.toString()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
