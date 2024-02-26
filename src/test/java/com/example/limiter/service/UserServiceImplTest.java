package com.example.limiter.service;

import com.example.limiter.dao.UserMySQLRepository;
import com.example.limiter.exception.LimiterException;
import com.example.limiter.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private ValidationService validationService;
    @Mock
    private DataSourceRouter dataSourceRouter;
    @Mock
    private UserMySQLRepository userMySQLRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Test getUser() method should return user")
    void testGetUser_UserExists() {
        String userId = "testId";
        User expectedUser = new User();
        when(dataSourceRouter.getRepository()).thenReturn(userMySQLRepository);
        when(userMySQLRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        User result = userService.getUser(userId);

        assertEquals(expectedUser, result, "The returned user should match the expected user");
    }

    @Test
    @DisplayName("Test getUser() method should throw LimiterException when user not found")
    void testGetUser_UserNotFound_ThrowsLimiterException() {
        String userId = "testId";
        when(dataSourceRouter.getRepository()).thenReturn(userMySQLRepository);
        when(userMySQLRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(LimiterException.class, () -> userService.getUser(userId));
    }

    @Test
    @DisplayName("Test createUser() method should save and return user")
    void testCreateUser_SuccessfulCreation() {
        User newUser = new User();
        when(dataSourceRouter.getRepository()).thenReturn(userMySQLRepository);
        when(userMySQLRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User createdUser = userService.createUser(newUser);

        assertNotNull(createdUser.getLastLoginTimeUtc(), "The last login time should not be null");
        assertEquals(0, createdUser.getQuota(), "The quota should be 0");
        assertFalse(createdUser.isBlocked(), "The user should not be blocked");
    }

    @Test
    @DisplayName("Test updateUser() method should update user")
    void testUpdateUser_UserUpdatedSuccessfully() {
        String userId = "testId";
        User updatedUser = new User(); // Populate this user as necessary
        when(dataSourceRouter.getRepository()).thenReturn(userMySQLRepository);
        when(userMySQLRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(userId, updatedUser);

        assertEquals(userId, result.getId());
        assertNotNull(result.getLastLoginTimeUtc());
    }

    @Test
    void testDeleteUser_UserDeletedSuccessfully() {
        String userId = "testId";
        User user = new User();
        when(dataSourceRouter.getRepository()).thenReturn(userMySQLRepository);
        when(userMySQLRepository.findById(userId)).thenReturn(Optional.of(user));

        User deletedUser = userService.deleteUser(userId);

        verify(dataSourceRouter.getRepository()).delete(user);
        assertEquals(user, deletedUser, "The returned user should match the deleted user");
    }

    @Test
    @DisplayName("Test getAllQuota() method should return all quotas")
    void testGetAllQuota_QuotaRetrievedSuccessfully() {
        // Prepare test data
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setQuota(5);

        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setQuota(200);

        Iterable<User> users = Arrays.asList(user1, user2);

        when(dataSourceRouter.getRepository()).thenReturn(userMySQLRepository);
        when(userMySQLRepository.findAll()).thenReturn(users);

        Map<String, Integer> quotaMap = userService.getAllQuota();

        assertAll(
                () -> assertNotNull(quotaMap, "The returned map should not be null."),
                () -> assertEquals(2, quotaMap.size(), "The map should contain exactly 2 entries."),
                () -> assertTrue(quotaMap.containsKey("John Doe"), "Map should contain an entry for John Doe."),
                () -> assertTrue(quotaMap.containsKey("Jane Smith"), "Map should contain an entry for Jane Smith."),
                () -> assertEquals(Integer.valueOf(5), quotaMap.get("John Doe"), "John Doe's quota should be 100."),
                () -> assertEquals(Integer.valueOf(200), quotaMap.get("Jane Smith"), "Jane Smith's quota should be 200."));
    }

}