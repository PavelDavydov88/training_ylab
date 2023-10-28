package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Клас DTO для ответа request JSON(String : List<String>)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseListDTO {
    private List<String> listOperation;
}
