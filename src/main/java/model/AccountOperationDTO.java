package model;

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
    private Long valueOperation;
    private Long transaction;
}
