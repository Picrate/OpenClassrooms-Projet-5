package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapperImpl;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserMapper Unit Tests")
public class UserMapperImplTests {

    User user;
    UserDto userDto;

    UserMapperImpl userMapper;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@studio.com");
        user.setPassword("password");

        userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@studio.com");
        userDto.setPassword("password");

        userMapper = new UserMapperImpl();
    }

    @Test
    void shouldReturnTeacherDtoIfValidTeacher() {
        UserDto expectedDto = userMapper.toDto(user);
        assertThat(expectedDto).isEqualTo(userDto);
    }

    @Test
    void shouldReturn_NullDto_If_Teacher_IsNull(){
        UserDto expectedDto = userMapper.toDto((User) null);
        assertThat(expectedDto).isNull();
    }

    @Test
    void shouldReturnTeacherIfValidTeacherDto() {
        User expectedUser = userMapper.toEntity(userDto);
        assertThat(expectedUser).isEqualTo(user);
    }

    @Test
    void shouldReturn_NullTeacher_If_TeacherDto_IsNull(){
        User expectedUser = userMapper.toEntity((UserDto) null);
        assertThat(expectedUser).isNull();
    }

    @Test
    void shouldReturn_TeacherDtoList_If_ValidTeacherList_Or_Null() {
        UserDto userDto2 = new UserDto();
        userDto2.setFirstName("Jane");
        userDto2.setLastName("Doe");
        userDto2.setEmail("jane.doe@studio.com");
        userDto2.setPassword("password");
        List<UserDto> dtoList = new ArrayList<>();
        dtoList.add(userDto);
        dtoList.add(userDto2);

        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setEmail("jane.doe@studio.com");
        user2.setPassword("password");
        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user2);

        List<UserDto> expectedDtoList = userMapper.toDto(userList);
        assertThat(expectedDtoList).isEqualTo(dtoList);
    }

    @Test
    void shouldReturn_NullDto_If_TeacherList_IsNull(){
        List<UserDto> expectedDtoList = userMapper.toDto((List<User>) null);
        assertThat(expectedDtoList).isNull();
    }

    @Test
    void shouldReturn_TeacherList_IfValidTeacherDtoList() {
        UserDto userDto2 = new UserDto();
        userDto2.setFirstName("Jane");
        userDto2.setLastName("Doe");
        userDto2.setEmail("jane.doe@studio.com");
        userDto2.setPassword("password");
        List<UserDto> dtoList = new ArrayList<>();
        dtoList.add(userDto);
        dtoList.add(userDto2);

        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setEmail("jane.doe@studio.com");
        user2.setPassword("password");
        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user2);

        List<User> expectedList = userMapper.toEntity(dtoList);
        assertThat(expectedList).isEqualTo(userList);
    }

    @Test
    void shouldReturn_NullTeacherList_If_TeacherDtoList_IsNull(){
        List<User> expectedList = userMapper.toEntity((List<UserDto>) null);
        assertThat(expectedList).isNull();
    }
}
