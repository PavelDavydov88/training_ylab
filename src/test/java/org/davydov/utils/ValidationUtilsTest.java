package org.davydov.utils;

import org.davydov.utils.ValidationUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilsTest {

    @Test
    void notEmpty() {
        assertThrows(RuntimeException.class, () -> ValidationUtils.isEmptyOrNull(""));
        assertThrows(RuntimeException.class, () -> ValidationUtils.isEmptyOrNull(null));
    }

    @Test
    void size() {
        assertThrows(RuntimeException.class, () -> ValidationUtils.size(1, 1, "test"));
    }
}