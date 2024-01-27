package workspace.application.domain.steps;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import workspace.application.domain.TestConfig.TestConfig;
import org.springframework.boot.test.context.SpringBootTest;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = TestConfig.class)
@Slf4j
public class CucumberSpringConfiguration {

}
