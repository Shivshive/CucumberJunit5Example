package workspace.application.runner;

import io.cucumber.spring.ScenarioScope;
import lombok.extern.java.Log;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.launcher.EngineFilter;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import workspace.application.domain.TestConfig.TestConfig;

import java.util.Map;
import static io.cucumber.junit.platform.engine.Constants.*;


@Log
@SpringBootTest(classes = TestConfig.class)
public class CustomLauncherTest {

    @Value("${parallel.thread.count}")
    private String threadCount;

    @BeforeAll
    public static void beforeAll(){
        log.info(" *************************** [[ before all executed ]] ******************************** ");
    }

    @Test
    public void runTest(){

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .filters(EngineFilter.includeEngines("cucumber"))
                .configurationParameters(Map.of(
                        GLUE_PROPERTY_NAME , "workspace.application.domain",
                        PLUGIN_PROPERTY_NAME, "pretty,summary,html:target/cucumber.html,json:target/cucumber-report/cucumber.json",
                        FEATURES_PROPERTY_NAME,"src/test/java/workspace/application/domain",
                        PARALLEL_EXECUTION_ENABLED_PROPERTY_NAME,"true",
                        PARALLEL_CONFIG_STRATEGY_PROPERTY_NAME,"fixed",
                        PARALLEL_CONFIG_FIXED_PARALLELISM_PROPERTY_NAME, threadCount
                ))
                .build();
        Launcher launcher = LauncherFactory.create();
        launcher.execute(request);

    }

    @AfterAll
    public static void afterAll(){
        log.info(" *************************** [[ after all executed ]] ******************************** ");
    }
}
