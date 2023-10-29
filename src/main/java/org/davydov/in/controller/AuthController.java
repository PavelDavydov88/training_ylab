package org.davydov.in.controller;

import lombok.RequiredArgsConstructor;
import org.davydov.model.PlayerDTO;
import org.davydov.model.ResponseDTO;
import org.davydov.service.AuthService;
import org.davydov.utils.ValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody PlayerDTO dto) {
        try {
            ValidationUtils.isEmptyOrNull(dto.getName());
            ValidationUtils.size(2, 10, dto.getName());
            Optional<String> token = authService.doAuthorization(dto);
            return new ResponseEntity<>(new ResponseDTO(token.orElse("default token")), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
