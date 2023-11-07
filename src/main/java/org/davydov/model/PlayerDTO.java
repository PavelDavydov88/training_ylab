package org.davydov.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull
    @NotEmpty
    @Size(min = 2, max = 10)
    private String name;

    @NotNull
    @NotEmpty
    @Size(min =3, max = 10)
    private String password;
}
