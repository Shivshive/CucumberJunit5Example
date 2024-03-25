package workspace.application.domain.core;

import io.cucumber.java.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import workspace.application.domain.TestConfig.CommonWorld;
import workspace.application.domain.actions.ScenarioContext;
import workspace.application.domain.core.network_api_interceptor_new.NetworkInterception;
import workspace.application.domain.reporter.CucumberReporter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Log
public class Hooks {

    @Autowired
    WebdriverFactory webdriverFactory;

    @Autowired
    ScenarioContext scenarioContext; // Its a data carrier between steps

    @Autowired
    NetworkInterception networkInterception;

    @Value("${network.intercept}")
    boolean networkIntercept;

    @Before("@ui")
    public void beforeScenario(Scenario scenario) {
        log.info("Before scenario hook started");
        log.info(String.format("Test common username ==> %s ", CommonWorld.commonConfig.getProperty("common_username")));
        webdriverFactory.initializeDriver();
        if (networkIntercept) {
            start_interception(scenario);
        }
    }

    @Before
    public void general_beforeScenario(Scenario scenario) {
        log.info("Setting up scenario in scenarioContext");
        scenarioContext.put("scenario", scenario);
    }

    @After("@ui")
    public void afterScenario(Scenario scenario) {
        log.info("After scenario hook started");
        webdriverFactory.removeDriver();
        if (networkIntercept) {
            attach_interception(scenario);
        }
    }

    public void start_interception(Scenario scenario) {
        networkInterception.setHarfileName(String.format("%s-%s.har", scenario.getName(), (DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-mm-ss-s").withZone(ZoneId.systemDefault()).format(Instant.now()))));
        networkInterception.startNetworkInterception();
    }

    public void attach_interception(Scenario scenario) {
        networkInterception.getHarAsBytes().ifPresent(harBytes -> {
            try {
                String harName = String.format("%s-%s", scenario.getName(), (DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-mm-ss-s").withZone(ZoneId.systemDefault()).format(Instant.now())));
                byte[] zipHarByte = CucumberReporter.zipFile(String.format("%s.har",harName),harBytes);
                scenario.attach(zipHarByte, "application",String.format("%s.zip",harName));
            } catch (Exception e) {
                log.info(String.format("Exception occured while attaching zip to report => %s", e.getMessage()));
            }
        });
    }

    @BeforeAll
    public static void beforeAllMethod() {
        log.info("**************** [[ BeforeAll Executed Cucumber ]] *******************");
    }

    @AfterAll
    public static void afterAllMethod() {
        log.info("**************** [[ AfterAll Executed Cucumber ]] *******************");
    }

}
