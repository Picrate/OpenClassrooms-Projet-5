package com.openclassrooms.starterjwt;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.services.TeacherService;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Check API operations on Teachers")
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TeacherControllerIntegrationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private HttpHeaders headers;
    private String token;

    @BeforeAll
    void setupBeforeAll() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");
        HttpEntity<LoginRequest> httpEntity = new HttpEntity<LoginRequest>(loginRequest);
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/login", httpEntity, String.class);
        DocumentContext context = JsonPath.parse(response.getBody());
        token = context.read("$.token", String.class);
        assertThat(token).isNotBlank();
        headers = new HttpHeaders();
        headers.setBearerAuth(token);
    }

    @Test
    void shouldReturn_ATeacher_If_Exists(){

        TeacherDto expectedTeacher = new TeacherDto(
                1L,
                "DELAHAYE",
                "Margot",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/teacher/1", HttpMethod.GET, httpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotBlank();

        DocumentContext context = JsonPath.parse(response.getBody());
        Number id = context.read("$.id");
        String lastName = context.read("$.lastName");
        String firstName = context.read("$.firstName");

        assertThat(id.longValue()).isEqualTo(expectedTeacher.getId());
        assertThat(lastName).isEqualTo(expectedTeacher.getLastName());
        assertThat(firstName).isEqualTo(expectedTeacher.getFirstName());
    }

    @Test
    void shouldNotReturn_ATeacher_IfNot_Exists(){

        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/teacher/99", HttpMethod.GET, httpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    void shouldReturnAnError_If_TeacherID_IsNot_ANumber(){

        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/teacher/NotANumber", HttpMethod.GET, httpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    void shouldReturn_AllTeachers(){

        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/teacher", HttpMethod.GET, httpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotBlank();

        DocumentContext context = JsonPath.parse(response.getBody());
        JSONArray teachers = context.read("$");
        assertThat(teachers).hasSize(2);
    }
}
