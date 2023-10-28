package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Клас DTO игрока для обработки данных на уровне сервлета и сервиса
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO {
    private String name;
    private String password;
}
