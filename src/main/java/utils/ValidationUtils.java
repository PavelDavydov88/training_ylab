package utils;

import lombok.experimental.UtilityClass;

/**
 * Класс для валидации данных
 */
@UtilityClass
public final class ValidationUtils {
    /**
     * Метод проверки параметра на empty и null
     *
     * @param field параметр для проверки
     */
    public static void isEmptyOrNull(String field) {
        if (field == null || field.isEmpty()) {
            throw new RuntimeException("Field is empty or null!");
        }
    }

    /**
     * Метод проверки параметра на значение min и max
     *
     * @param min   уставка для значения min
     * @param max   уставка для значения max
     * @param field параметр для проверки
     */
    public static void size(int min, int max, String field) {
        if (field.length() < min || field.length() > max) {
            throw new RuntimeException("Field must be in area " + min + " to " + max + "!");
        }
    }
}