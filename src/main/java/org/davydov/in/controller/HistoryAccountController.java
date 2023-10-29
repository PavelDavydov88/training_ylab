package org.davydov.in.controller;

import lombok.RequiredArgsConstructor;
import org.davydov.model.AccountOperationDTO;
import org.davydov.model.PlayerDTO;
import org.davydov.model.ResponseDTO;
import org.davydov.model.ResponseListDTO;
import org.davydov.service.AuthService;
import org.davydov.service.HistoryCreditDebitService;
import org.davydov.utils.ValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class HistoryAccountController {

    private final HistoryCreditDebitService historyCreditDebitService;

    @PostMapping("/history")
    public ResponseEntity<?> history(@RequestHeader Map<String, String> headers) {
        String token = headers.get("token");
        try {
            if (token == null) {
                throw new RuntimeException("token is null");
            }
            List<String> listOperation = historyCreditDebitService.getListOperationAccount(token);
            return new ResponseEntity<>(new ResponseListDTO(listOperation), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
