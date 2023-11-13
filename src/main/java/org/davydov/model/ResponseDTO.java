package org.davydov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Клас DTO для ответа request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
    private String response;
}
