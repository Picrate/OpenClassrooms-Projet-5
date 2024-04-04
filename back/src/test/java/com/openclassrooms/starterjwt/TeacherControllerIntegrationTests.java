package com.openclassrooms.starterjwt;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.starterjwt.dto.SessionDto;
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

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Check CRUD operation on Sessions")
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class TeacherControllerIntegrationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TeacherService teacherService;

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
    void shouldReturn_ASession_If_Exists(){

        Session expectedSession = new Session();
        expectedSession.setId(1L);
        expectedSession.setName("Session 1");
        expectedSession.setDescription("First Yoga Session");

        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/session/1", HttpMethod.GET, httpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotBlank();

        DocumentContext context = JsonPath.parse(response.getBody());
        Number id = context.read("$.id");
        String sessionName = context.read("$.name");
        String sessionDescription = context.read("$.description");

        assertThat(id.longValue()).isEqualTo(expectedSession.getId());
        assertThat(sessionName).isEqualTo(expectedSession.getName());
        assertThat(sessionDescription).isEqualTo(expectedSession.getDescription());
    }

    @Test
    void shouldNotReturn_ASession_If_NotExists(){

        Session expectedSession = new Session();
        expectedSession.setId(1L);
        expectedSession.setName("Session 1");
        expectedSession.setDescription("First Yoga Session");

        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/session/99", HttpMethod.GET, httpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    void shouldReturn_AnError_If_SessionIdIsNotANumber_InGET(){

        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/session/notANumber", HttpMethod.GET, httpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    void shouldReturn_AllSessions(){

        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/session", HttpMethod.GET, httpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotBlank();

        DocumentContext context = JsonPath.parse(response.getBody());
        Number count = context.read("$.length()");
        assertThat(count).isEqualTo(2);
        JSONArray sessionNames = context.read("$..name");
        assertThat(sessionNames).containsExactlyInAnyOrder("Session 1", "Session 2");
    }

    @Test
    @DirtiesContext
    void should_Create_NewSession(){
        SessionDto newSession = new SessionDto();
        newSession.setName("Session 3");
        newSession.setDescription("Description Session 3");
        newSession.setDate(new Date());
        newSession.setTeacher_id(1L);

        HttpEntity<SessionDto> httpEntity = new HttpEntity<SessionDto>(newSession, headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/session", HttpMethod.POST, httpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext context = JsonPath.parse(response.getBody());
        Number id = context.read("$.id");
        String sessionName = context.read("$.name");
        String sessionDescription = context.read("$.description");

        assertThat(id.longValue()).isEqualTo(3L);
        assertThat(sessionName).isEqualTo("Session 3");
        assertThat(sessionDescription).isEqualTo("Description Session 3");
    }



    @Test
    @DirtiesContext
    void shouldNot_CreateNewSession_With_EmptyName(){
        SessionDto newSession = new SessionDto();
        newSession.setName("");
        newSession.setDescription("Description Session 3");
        newSession.setDate(new Date());
        newSession.setTeacher_id(1L);

        HttpEntity<SessionDto> httpEntity = new HttpEntity<SessionDto>(newSession, headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/session", HttpMethod.POST, httpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    @DirtiesContext
    void should_UpdateSession(){
        SessionDto updatedSession = new SessionDto();
        updatedSession.setName("New Session 1");
        updatedSession.setDescription("New Description Session 1");
        updatedSession.setDate(new Date());
        updatedSession.setTeacher_id(2L);

        HttpEntity<SessionDto> httpEntity = new HttpEntity<SessionDto>(updatedSession, headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/session/1", HttpMethod.PUT, httpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotBlank();

        DocumentContext context = JsonPath.parse(response.getBody());
        Number id = context.read("$.id");
        String sessionName = context.read("$.name");
        String sessionDescription = context.read("$.description");
        Number teacherId = context.read("$.teacher_id");

        assertThat(id.longValue()).isEqualTo(1L);
        assertThat(sessionName).isEqualTo(updatedSession.getName());
        assertThat(sessionDescription).isEqualTo(updatedSession.getDescription());
        assertThat(teacherId.longValue()).isEqualTo(updatedSession.getTeacher_id());
    }

    @Test
    void shouldReturn_AnError_If_SessionIdIsNotANumber_InPUT(){

        SessionDto updatedSession = new SessionDto();
        updatedSession.setName("New Session 1");
        updatedSession.setDescription("New Description Session 1");
        updatedSession.setDate(new Date());
        updatedSession.setTeacher_id(2L);

        HttpEntity<SessionDto> httpEntity = new HttpEntity<SessionDto>(updatedSession, headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/session/NotANumber", HttpMethod.PUT, httpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    @DirtiesContext
    void should_DeleteSession(){

        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/session/1", HttpMethod.DELETE, httpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isBlank();

        response = testRestTemplate.exchange("/api/session/1", HttpMethod.GET, httpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();

    }

    @Test
    void shouldReturn_AnError_If_SessionIdIsNotANumber_InDELETE(){

        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/session/NotANumber", HttpMethod.DELETE, httpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    @DirtiesContext
    void should_Participate_ToASession(){

        // Subscribe User 2 To Session 1
        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/session/1/participate/2", HttpMethod.POST, httpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isBlank();

        // Check User 2 has been subscribed to session 1
        response = testRestTemplate.exchange("/api/session/1", HttpMethod.GET, httpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotBlank();

        DocumentContext context = JsonPath.parse(response.getBody());
        Number id = context.read("$.id");
        JSONArray users = context.read("$.users");

        assertThat(id.longValue()).isEqualTo(1L);
        assertThat(users).containsExactlyInAnyOrder(2);
    }

    @Test
    @DirtiesContext
    void should_UnParticipate_ToASession(){

        // Check User 2 has been subscribed to session 2
        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/session/2", HttpMethod.GET, httpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotBlank();

        DocumentContext context = JsonPath.parse(response.getBody());
        Number id = context.read("$.id");
        JSONArray users = context.read("$.users");
        assertThat(id.longValue()).isEqualTo(2L);
        assertThat(users).containsExactlyInAnyOrder(2);

        // UnSubscribe User 2 To Session 1
         response = testRestTemplate.exchange("/api/session/2/participate/2", HttpMethod.DELETE, httpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isBlank();

        // Check User 2 has been unsubscribed to session 2
        response = testRestTemplate.exchange("/api/session/2", HttpMethod.GET, httpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotBlank();

        context = JsonPath.parse(response.getBody());
        users = context.read("$.users");
        assertThat(users).doesNotContain(2);
    }

}
