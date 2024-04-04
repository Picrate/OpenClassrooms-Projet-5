package com.openclassrooms.starterjwt;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Check Login / Logout / Register Features")
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testLogin_Succeeded(){
        //Scenario: User want to log in the api
        // Given
        // I give my email account
        // And I give my password
        JwtResponse loginResponse = new JwtResponse("", 1L, "yoga@studio.com", "Admin", "Admin", true);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");
        HttpEntity<LoginRequest> httpEntity = new HttpEntity<LoginRequest>(loginRequest);
        // When I submit my credentials
        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/login", httpEntity, String.class);
        // Then
        // The Server Response should Be 200 - OK
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // And response body should contain my account informations
        // And if I have admin role
        // And an authentication token
        DocumentContext context = JsonPath.parse(response.getBody());
        Number id = context.read("$.id", Number.class);
        String token = context.read("$.token", String.class);
        String username = context.read("$.username", String.class);
        String firstname = context.read("$.firstName", String.class);
        String lastname = context.read("$.lastName", String.class);
        Boolean isAdmin = context.read("$.admin", Boolean.class);
        assertThat(id.longValue()).isEqualTo(loginResponse.getId());
        assertThat(token).isNotBlank();
        assertThat(username).isEqualTo(loginResponse.getUsername());
        assertThat(firstname).isEqualTo(loginResponse.getFirstName());
        assertThat(lastname).isEqualTo(loginResponse.getLastName());
        assertThat(isAdmin).isTrue();
    }

    @Test
    void testLogin_Fail_With_Empty_EmailOrPassword(){
        //Scenario: User want to log in the api with an empty email or empty password
        // Given
        // I give an empty email
        // And I give my password
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("");
        loginRequest.setPassword("test!1234");
        HttpEntity<LoginRequest> httpEntity = new HttpEntity<LoginRequest>(loginRequest);
        // When I try to log in
        ResponseEntity<Void> response = restTemplate.postForEntity("/api/auth/login", httpEntity, Void.class);
        // Then
        // The Server Response should Be 400 - BAD REQUEST
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        // And the response Body is Null;
        assertThat(response.getBody()).isNull();
        // Or
        // Given
        // I give my email
        // And I give an Empty password
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("");
        // When I try to log in
        response = restTemplate.postForEntity("/api/auth/login", httpEntity, Void.class);
        // Then I should receive same response from server
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void testJWTAuthenticationOK(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");
        HttpEntity<LoginRequest> httpEntity = new HttpEntity<LoginRequest>(loginRequest);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/login", httpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext context = JsonPath.parse(response.getBody());
        String token = context.read("$.token", String.class);
        assertThat(token).isNotBlank();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> sessionHttpEntity = new HttpEntity<>("",headers);
        response = restTemplate.exchange("/api/session", HttpMethod.GET, sessionHttpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testUnauthorizedAccess_ShouldReturn_ErrorMessage(){
        ResponseEntity<String> response = restTemplate.getForEntity("/api/session", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        DocumentContext context = JsonPath.parse(response.getBody());
        String path = context.read("$.path", String.class);
        assertThat(path).isEqualTo("/api/session");
        String error = context.read("$.error", String.class);
        assertThat(error).isEqualTo("Unauthorized");
        String message = context.read("$.message", String.class);
        assertThat(message).isEqualTo("Full authentication is required to access this resource");
        Number status = context.read("$.status", Number.class);
        assertThat(status).isEqualTo(401);
    }

    @Test
    @DirtiesContext
    void testRegisterAsNewUser_Succeeded(){
        // Given User want to register
        SignupRequest request = new SignupRequest();
        request.setEmail("test-user@test.local");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setPassword("test!1234");
        // When I submit my account informations to register
        HttpEntity<SignupRequest> entity = new HttpEntity<>(request);
        ResponseEntity<String> response =  testRestTemplate.postForEntity("/api/auth/register", entity, String.class);
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotBlank();
        DocumentContext context = JsonPath.parse(response.getBody());
        String message = context.read("$.message");
        assertThat(message).isNotBlank();
        assertThat(message).isEqualTo("User registered successfully!");
    }

}
