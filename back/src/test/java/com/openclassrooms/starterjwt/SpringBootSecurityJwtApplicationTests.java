package com.openclassrooms.starterjwt;

import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@Suite
@SelectClasses(
		{
				AuthControllerIntegrationTest.class,
				SessionControllerIntegrationTests.class,
				TeacherControllerIntegrationTests.class,
				UserControllerIntegrationTests.class
		}
)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootSecurityJwtApplicationTests {

	@Test
	public void contextLoads() {}
}
