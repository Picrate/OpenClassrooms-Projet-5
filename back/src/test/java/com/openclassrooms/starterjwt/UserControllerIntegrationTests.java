package com.openclassrooms.starterjwt;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
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

@DisplayName("Check API operations on Users")
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerIntegrationTests {

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
    void shouldReturn_AUser_If_Exists(){

        UserDto expectedUser = new UserDto();
        expectedUser.setId(1L);
        expectedUser.setEmail("yoga@studio.com");
        expectedUser.setFirstName("Admin");
        expectedUser.setLastName("Admin");
        expectedUser.setAdmin(true);

        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/user/1", HttpMethod.GET, httpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotBlank();

        DocumentContext context = JsonPath.parse(response.getBody());
        Number id = context.read("$.id");
        String firstName = context.read("$.firstName");
        String lastName = context.read("$.lastName");
        String email = context.read("$.email");
        boolean admin = context.read("$.admin");

        assertThat(id.longValue()).isEqualTo(expectedUser.getId());
        assertThat(firstName).isEqualTo(expectedUser.getFirstName());
        assertThat(lastName).isEqualTo(expectedUser.getLastName());
        assertThat(email).isEqualTo(expectedUser.getEmail());
        assertThat(admin).isEqualTo(expectedUser.isAdmin());
    }

    @Test
    void shouldNotReturn_AUser_If_NotExists(){

        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/user/99", HttpMethod.GET, httpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    void should_ReturnAnError_If_USerIdIsNotANumber_InGET(){

        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/user/notANumber", HttpMethod.GET, httpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    @DirtiesContext
    void should_DeleteUser_If_Himself(){

        // Login as User 2
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@studio.com");
        loginRequest.setPassword("test!1234");
        HttpEntity<LoginRequest> httpLoginEntity = new HttpEntity<LoginRequest>(loginRequest);
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/login", httpLoginEntity, String.class);
        DocumentContext context = JsonPath.parse(response.getBody());
        String userToken = context.read("$.token", String.class);
        assertThat(userToken).isNotBlank();
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(userToken);
        // Delete "My Account" as User 2
        HttpEntity<String> httpEntity = new HttpEntity<>("", userHeaders);
        response = testRestTemplate.exchange("/api/user/2", HttpMethod.DELETE, httpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isBlank();

        // Request as User Admin & check user 2 doen't exist
        httpEntity = new HttpEntity<>("", headers);
        response = testRestTemplate.exchange("/api/user/2", HttpMethod.GET, httpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();

    }

    @Test
    @DirtiesContext
    void shouldNot_DeleteUser_IfNot_Exists(){

        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/user/99", HttpMethod.DELETE, httpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    @DirtiesContext
    void shouldNot_DeleteUser_IfNot_Himself(){

        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/user/2", HttpMethod.DELETE, httpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    void shouldReturn_AnError_If_UserId_IsNotANumber_InDELETE(){

        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/api/user/NotANumber", HttpMethod.DELETE, httpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isBlank();
    }
}
