package com.example.limiter.service;

import com.example.limiter.exception.LimiterException;
import com.example.limiter.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class QuotaServiceImplTest {

    @InjectMocks
    private QuotaServiceImpl quotaService;

    @Mock
    private UserService userService;

    @Mock
    private ValidationService validationService;

    @Test
    @DisplayName("Test getConsumerQuota() method should return quota for user")
    void testGetConsumerQuota() {
        String userId = "user1";
        var expectedQuota = 5;
        User user = new User();
        user.setQuota(5);
        when(userService.getUser(userId)).thenReturn(user);
        doNothing().when(validationService).validateUserId(userId);

        Integer resultQuota = quotaService.getConsumerQuota(userId);

        assertEquals(expectedQuota, resultQuota, "The returned quota should match the expected quota (5)");
        verify(validationService, times(1)).validateUserId(userId);
        verify(userService, times(1)).getUser(userId);
    }

    @Test
    @DisplayName("Test getConsumerQuota() method should return quota for all user")
    void testGetUsersQuota() {
        Map<String, Integer> expectedQuotas = Map.of("user1", 5, "user2", 3, "user3", 8);
        when(userService.getAllQuota()).thenReturn(expectedQuotas);

        Map<String, Integer> actualQuotas = quotaService.getUsersQuota();

        assertNotNull(actualQuotas, "The returned quotas map should not be null");
        assertEquals(expectedQuotas.size(), actualQuotas.size(), "The size of the quotas map should match the expected size");
        for (Map.Entry<String, Integer> entry : expectedQuotas.entrySet()) {
            String userId = entry.getKey();
            Integer expectedQuota = entry.getValue();
            assertTrue(actualQuotas.containsKey(userId), "The quotas map should contain userId: " + userId);
            assertEquals(expectedQuota, actualQuotas.get(userId), "The quota for userId " + userId + " should match the expected quota");
        }

        verify(userService, times(1)).getAllQuota();
        verifyNoMoreInteractions(userService);

    }

    @Test
    @DisplayName("Test isRequestPermitted() method should return true if request is permitted")
    void testIsRequestPermitted() {
        String userId = "user1";
        User user = new User();
        user.setId(userId);
        user.setQuota(1);
        when(userService.getUser(userId)).thenReturn(user);
        doNothing().when(validationService).validateUserId(userId);
        ReflectionTestUtils.setField(quotaService, "quota", 5);

        boolean permitted = quotaService.isRequestPermitted(userId);

        assertTrue(permitted, "The request should be permitted");
        assertEquals(2, user.getQuota(), "The user's quota should be updated");
        verify(userService, times(1)).getUser(userId);

    }

    @Test
    @DisplayName("Test isRequestPermitted() method should throw LimiterException if request is not permitted")
    void testIsRequestPermitted_QuotaExceeded() {
        ReflectionTestUtils.setField(quotaService, "quota", 5);
        String userId = "user2";
        User user = new User();
        user.setId(userId);
        user.setQuota(5);
        when(userService.getUser(userId)).thenReturn(user);
        doNothing().when(validationService).validateUserId(userId);

        Exception exception = assertThrows(LimiterException.class, () -> quotaService.isRequestPermitted(userId), "The exception should be thrown");

        String expectedMessage = "Quota was reached. User is blocked";
        String actualMessage = exception.getMessage();
        assertAll(
        () -> assertTrue(actualMessage.contains(expectedMessage), "The exception message should contain the expected message"),
        () -> assertEquals(6, user.getQuota(), "The user's quota should be updated"),
        () -> assertTrue(user.isBlocked(), "The user should be blocked"));
    }


}
