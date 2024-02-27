package workspace.application.runner;

import lombok.extern.java.Log;
import org.junit.jupiter.api.*;
import org.junit.platform.launcher.EngineFilter;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import workspace.application.domain.TestConfig.CommonWorld;
import workspace.application.domain.TestConfig.TestConfig;
import workspace.application.domain.TestConfig.EnvConfig;
import workspace.application.domain.reporter.CucumberReporter;

import java.util.Map;
import static io.cucumber.junit.platform.engine.Constants.*;


@Log
@SpringBootTest(classes = {TestConfig.class, EnvConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Junit tag to keep only one instance of this class for each test hence eventually allowing us to keep BeforeAll/AfterAll as non-static
public class CustomLauncherTest {

    @Value("${parallel.thread.count}")
    private String threadCount;

    @BeforeAll
    public void beforeAll(){
        log.info(" *************************** [[ before all executed ]] ******************************** ");
        var username = CommonWorld.commonConfig.getProperty("common_username"); // Just an example on how to access commonConfig in beforeHook
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
        SummaryGeneratingListener summaryGeneratingListener = new SummaryGeneratingListener();
        launcher.execute(request, new TestExecutionListener[] {
            summaryGeneratingListener
        });

        if(!summaryGeneratingListener.getSummary().getFailures().isEmpty()){
            summaryGeneratingListener.getSummary().getFailures().forEach(test -> {
                log.severe(String.format("FAILURE LOG -- [ %s ] is Failed, with ERROR -> ",test.getTestIdentifier().getDisplayName(), test.getException().getMessage()));
            });
            Assertions.fail();
        }
    }

    @AfterAll
    public void afterAll(){
        CucumberReporter.setupReport();
        log.info(" *************************** [[ after all executed ]] ******************************** ");
    }
}
