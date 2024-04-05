package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapperImpl;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TeacherMapper Unit Tests")
public class TeacherMapperImplTests {

    Teacher teacher;
    TeacherDto teacherDto;

    TeacherMapperImpl teacherMapper;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        teacherDto = new TeacherDto();
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");

        teacherMapper = new TeacherMapperImpl();
    }

    @Test
    void shouldReturnTeacherDtoIfValidTeacher() {
        TeacherDto expectedTeacherDto = teacherMapper.toDto(teacher);
        assertThat(expectedTeacherDto).isEqualTo(teacherDto);
    }

    @Test
    void shouldReturn_NullDto_If_Teacher_IsNull(){
        TeacherDto expectedDto = teacherMapper.toDto((Teacher) null);
        assertThat(expectedDto).isNull();
    }

    @Test
    void shouldReturnTeacherIfValidTeacherDto() {
        Teacher expectedTeacher = teacherMapper.toEntity(teacherDto);
        assertThat(expectedTeacher).isEqualTo(teacher);
    }

    @Test
    void shouldReturn_NullTeacher_If_TeacherDto_IsNull(){
        Teacher expectedTeacher = teacherMapper.toEntity((TeacherDto) null);
        assertThat(expectedTeacher).isNull();
    }

    @Test
    void shouldReturn_TeacherDtoList_If_ValidTeacherList_Or_Null() {
        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setFirstName("Jane");
        teacherDto2.setLastName("Doe");
        List<TeacherDto> dtoList = new ArrayList<TeacherDto>();
        dtoList.add(teacherDto);
        dtoList.add(teacherDto2);

        Teacher teacher2 = new Teacher();
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Doe");
        List<Teacher> teacherList = new ArrayList<Teacher>();
        teacherList.add(teacher);
        teacherList.add(teacher2);

        List<TeacherDto> expectedTeacherDtoList = teacherMapper.toDto(teacherList);
        assertThat(expectedTeacherDtoList).isEqualTo(dtoList);
    }

    @Test
    void shouldReturn_NullDto_If_TeacherList_IsNull(){
        List<TeacherDto> expectedDtoList = teacherMapper.toDto((List<Teacher>) null);
        assertThat(expectedDtoList).isNull();
    }

    @Test
    void shouldReturn_TeacherList_IfValidTeacherDtoList() {
        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setFirstName("Jane");
        teacherDto2.setLastName("Doe");
        List<TeacherDto> dtoList = new ArrayList<TeacherDto>();
        dtoList.add(teacherDto);
        dtoList.add(teacherDto2);

        Teacher teacher2 = new Teacher();
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Doe");
        List<Teacher> teacherList = new ArrayList<Teacher>();
        teacherList.add(teacher);
        teacherList.add(teacher2);

        List<Teacher> expectedTeacherList = teacherMapper.toEntity(dtoList);
        assertThat(expectedTeacherList).isEqualTo(teacherList);
    }

    @Test
    void shouldReturn_NullTeacherList_If_TeacherDtoList_IsNull(){
        List<Teacher> expectedTeacherList = teacherMapper.toEntity((List<TeacherDto>) null);
        assertThat(expectedTeacherList).isNull();
    }
}
