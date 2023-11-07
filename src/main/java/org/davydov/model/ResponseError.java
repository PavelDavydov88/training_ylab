package org.davydov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Клас DTO для BAD_REQUEST
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseError {
    private String response;
}
