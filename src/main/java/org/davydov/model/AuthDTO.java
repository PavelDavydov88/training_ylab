package org.davydov.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;

/**
 * Клас DTO игрока для Auth
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthDTO {
    @NotNull
    @NotEmpty
    @Size(min = 2, max = 10)
    private String name;

    @NotNull
    @NotEmpty
    @Size(min =3, max = 10)
    private String password;

    @Min(1)
    @Max(Long.MAX_VALUE)
    private long idPlayer;
}
