package org.davydov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Клас DTO для ответа request JSON(String : String)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    private String response;
}
