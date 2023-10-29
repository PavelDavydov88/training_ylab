package org.davydov.in.controller;

import lombok.RequiredArgsConstructor;
import org.davydov.model.ResponseDTO;
import org.davydov.model.ResponseListDTO;
import org.davydov.service.AuditService;
import org.davydov.service.HistoryCreditDebitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @PostMapping("/audit")
    public ResponseEntity<?> audit(@RequestHeader Map<String, String> headers) {
        String token = headers.get("token");
        try {
            if (token == null) {
                throw new RuntimeException("token is null");
            }
            List<String> listAudit = auditService.getListAuditAction(token);
            return new ResponseEntity<>(new ResponseListDTO(listAudit), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
