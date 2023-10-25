package utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ValidationUtils {
    public static void isEmptyOrNull(String field) {
        if (field == null || field.isEmpty()) {
            throw new RuntimeException("Field is empty or null!");
        }
    }

    public static void size(int min, int max, String field) {
        if (field.length() < min || field.length() > max) {
            throw new RuntimeException("Field must be in area " + min + " to " + max + "!");
        }
    }
}