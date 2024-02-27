package workspace.application.domain.steps;

import lombok.extern.slf4j.Slf4j;
import workspace.application.domain.TestConfig.TestConfig;
import org.springframework.boot.test.context.SpringBootTest;
import io.cucumber.spring.CucumberContextConfiguration;
import workspace.application.domain.TestConfig.EnvConfig;

@CucumberContextConfiguration
@SpringBootTest(classes = {TestConfig.class,EnvConfig.class})
@Slf4j
public class CucumberSpringConfiguration {

}
