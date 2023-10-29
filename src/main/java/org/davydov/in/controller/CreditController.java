package org.davydov.in.controller;

import com.sun.net.httpserver.Headers;
import lombok.RequiredArgsConstructor;
import org.davydov.model.AccountOperationDTO;
import org.davydov.model.PlayerDTO;
import org.davydov.model.ResponseDTO;
import org.davydov.service.AuthService;
import org.davydov.service.PlayerService;
import org.davydov.utils.ValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.ServerRequest;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CreditController {

    private final PlayerService playerService;

    @PostMapping("/credit")
    public ResponseEntity<?> credit(@RequestHeader Map<String, String> headers, @RequestBody AccountOperationDTO dto) {
        String token = headers.get("token");
        try {
            if (token == null) {
                throw new RuntimeException("token is null");
            }
            Long accountPlayer = playerService.creditAccount(token, dto);
            return new ResponseEntity<>(new ResponseDTO(accountPlayer.toString()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
