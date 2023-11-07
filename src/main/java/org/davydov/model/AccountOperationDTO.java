package org.davydov.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Клас DTO для операции кредит/дебит игрока
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountOperationDTO {
    @Min(1)
    @Max(Long.MAX_VALUE)
    private long valueOperation;

    @Min(1)
    @Max(Long.MAX_VALUE)
    private long transaction;
}
