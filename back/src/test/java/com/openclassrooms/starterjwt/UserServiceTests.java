package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("UserService Unit Tests")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);

    }

    @Test
    void shouldReturn_UserById_If_Exists() {
        User expectedUser = new User();
        expectedUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));

        final User resultUser = userService.findById(1L);
        verify(userRepository, times(1)).findById(1L);
        assertThat(resultUser).isEqualTo(expectedUser);
    }

    @Test
    void shouldReturn_Null_IfUser_NotExists() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        final User resultUser = userService.findById(99L);
        verify(userRepository, times(1)).findById(99L);
        assertThat(resultUser).isNull();
    }
}
