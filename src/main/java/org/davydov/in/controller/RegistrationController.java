package org.davydov.in.controller;

import io.swagger.annotations.*;
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

//@Api(tags = "Product Controller")
//@SwaggerDefinition(tags = {
//        @Tag(name = "Product Controller", description = "Write description here")
//})
@Api(value = "RegistrationController", tags = {"Product Controller"})
//@SwaggerDefinition(tags = {
//        @Tag(name = "Product Controller", description = "Write description here")
//})
@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final PlayerService playerService;


    @ApiOperation(value = "List of all products", response = PlayerDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "404 error")
    })
//    @ApiOperation(value = "registration of player")
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
