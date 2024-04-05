package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("TeacherService Unit Tests")
@ExtendWith(MockitoExtension.class)
public class TeacherServiceTests {

    @Mock
    TeacherRepository teacherRepository;

    TeacherService teacherService;

    @BeforeEach
    void setUp() {
        teacherService = new TeacherService(teacherRepository);
    }

    @Test
    void should_ReturnTeacher_If_Exists() {
        Teacher expectedTeacher = new Teacher();
        expectedTeacher.setId(1L);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(expectedTeacher));

        final Teacher resultTeacher = teacherService.findById(1L);
        verify(teacherRepository, times(1)).findById(1L);
        assertThat(resultTeacher).isEqualTo(expectedTeacher);
    }

    @Test
    void should_ReturnNull_If_TeacherNotExists() {
        when(teacherRepository.findById(99L)).thenReturn(Optional.empty());

        final Teacher resultTeacher = teacherService.findById(99L);
        verify(teacherRepository, times(1)).findById(99L);
        assertThat(resultTeacher).isNull();
    }

    @Test
    void shoud_Return_TeacherList(){

        Teacher expectedTeacher = new Teacher();
        expectedTeacher.setId(1L);
        Teacher expectedTeacher2 = new Teacher();
        expectedTeacher2.setId(2L);
        List<Teacher> expectedList = new ArrayList<Teacher>();
        expectedList.add(expectedTeacher);
        expectedList.add(expectedTeacher2);

        when(teacherRepository.findAll()).thenReturn(expectedList);

        List<Teacher> resultList = teacherService.findAll();
        verify(teacherRepository, times(1)).findAll();
        assertThat(resultList).isEqualTo(expectedList);
    }

}
