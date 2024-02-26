package com.example.limiter.service;

import com.example.limiter.exception.LimiterException;
import com.example.limiter.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidationServiceImplTest {

    private ValidationServiceImpl validationService;

    @BeforeEach
    void setUp() {
        validationService = new ValidationServiceImpl();
    }

    @Test
    @DisplayName("validate UserId with valid userId should not throw exception")
    void validateUserId_withValidUserId_shouldNotThrowException() {
        assertDoesNotThrow(() -> validationService.validateUserId("validUserId"), "validateUserId with valid userId should not throw exception");
    }

    @Test
    @DisplayName("validate UserId with null userId should throw LimiterException")
    void validateUserId_withNullUserId_shouldThrowLimiterException() {
        Exception exception = assertThrows(LimiterException.class, () -> validationService.validateUserId(null));
        assertEquals("UserId is null or empty", exception.getMessage(), "validateUserId with null userId should throw LimiterException");
    }

    @Test
    @DisplayName("validate UserId with blank userId should throw LimiterException")
    void validateUserId_withBlankUserId_shouldThrowLimiterException() {
        Exception exception = assertThrows(LimiterException.class, () -> validationService.validateUserId(" "));
        assertEquals("UserId is null or empty", exception.getMessage(), "validateUserId with blank userId should throw LimiterException");
    }

    @Test
    @DisplayName("validate User with valid user should not throw exception")
    void validateUser_withValidUser_shouldNotThrowException() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        assertDoesNotThrow(() -> validationService.validateUser(user), "validateUser with valid user should not throw exception");
    }

    @Test
    @DisplayName("validate User with null user should throw LimiterException")
    void validateUser_withNullUser_shouldThrowLimiterException() {
        Exception exception = assertThrows(LimiterException.class, () -> validationService.validateUser(null));
        assertEquals("User is null", exception.getMessage(), "validateUser with null user should throw LimiterException");
    }

    @Test
    @DisplayName("validate User with invalid first name should throw LimiterException")
    void validateUser_withInvalidFirstName_shouldThrowLimiterException() {
        User user = new User();
        user.setFirstName(" ");
        user.setLastName("Doe");
        Exception exception = assertThrows(LimiterException.class, () -> validationService.validateUser(user));
        assertEquals("First name is null or empty", exception.getMessage(), "validateUser with invalid first name should throw LimiterException");
    }

    @Test
    @DisplayName("validate User with invalid last name should throw LimiterException")
    void validateUser_withInvalidLastName_shouldThrowLimiterException() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName(" ");
        Exception exception = assertThrows(LimiterException.class, () -> validationService.validateUser(user));
        assertEquals("Last name is null or empty", exception.getMessage(), "validateUser with invalid last name should throw LimiterException");
    }

    @Test
    @DisplayName("validate UserIdAndUser with null userId should throw LimiterException")
    void validateUserIdAndUser_withValidInputs_shouldNotThrowException() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        assertDoesNotThrow(() -> validationService.validateUserIdAndUser("validUserId", user), "validateUserIdAndUser with valid inputs should not throw exception");
    }

}
