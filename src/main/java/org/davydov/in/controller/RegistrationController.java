package org.davydov.in.controller;

import lombok.RequiredArgsConstructor;
import org.davydov.model.PlayerDTO;
import org.davydov.model.ResponseDTO;
import org.davydov.service.PlayerService;
import org.davydov.utils.ValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для регистрации игрока
 */
@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final PlayerService playerService;

    /**
     * Метод для регистрации игрока
     *
     * @param dto DTO игрока
     * @return уведомление о регистрации игрока
     */
    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody PlayerDTO dto) {
        try {
            ValidationUtils.isEmptyOrNull(dto.getName());
            ValidationUtils.isEmptyOrNull(dto.getPassword());
            ValidationUtils.size(2, 10, dto.getName());
            ValidationUtils.size(3, 10, dto.getPassword());
            playerService.create(dto);
            return new ResponseEntity<>(new ResponseDTO("Player created!"), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
