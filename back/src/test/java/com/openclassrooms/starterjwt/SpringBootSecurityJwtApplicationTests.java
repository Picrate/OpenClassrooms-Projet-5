package com.openclassrooms.starterjwt;

import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@Suite
@SelectClasses(
		{
				UserServiceTests.class,
				TeacherServiceTests.class,
				UserMapperImplTests.class,
				TeacherMapperImplTests.class,
				AuthControllerIntegrationTest.class,
				SessionControllerIntegrationTests.class,
				TeacherControllerIntegrationTests.class,
				UserControllerIntegrationTests.class
		}
)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootSecurityJwtApplicationTests {

	@Test
	public void contextLoads(ApplicationContext context) {
		assertThat(context).isNotNull();
	}
}
