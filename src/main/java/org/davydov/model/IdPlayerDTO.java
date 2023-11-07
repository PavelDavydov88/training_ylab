package org.davydov.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Клас DTO для ответа request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdPlayerDTO {
    @Min(1)
    @Max(Long.MAX_VALUE)
    private long idPlayer;
}
