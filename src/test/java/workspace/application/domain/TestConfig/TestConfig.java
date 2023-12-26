package workspace.application.domain.TestConfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "workspace.application.domain", "workspace.application.runner"})
public class TestConfig {
}