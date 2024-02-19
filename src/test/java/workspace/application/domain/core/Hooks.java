package workspace.application.domain.core;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import workspace.application.domain.core.network_api_interceptor_new.NetworkInterception;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Log
public class Hooks {

    @Autowired
    WebdriverFactory webdriverFactory;

    @Autowired
    NetworkInterception networkInterception;

    @Before("@ui")
    public void beforeScenario(Scenario scenario){
        log.info("Before scenario hook started");
        webdriverFactory.initializeDriver();
        log.info("Before scenario hook executed");
        start_interception(scenario);
    }

    @After("@ui")
    public void afterScenario(Scenario scenario){
        log.info("After scenario hook started");
        webdriverFactory.removeDriver();
        log.info("After scenario hook executed");
        attach_interception(scenario);
    }

    public void start_interception(Scenario scenario){
        networkInterception.setHarfileName(String.format("%s-%s.har",scenario.getName(),(DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-mm-ss-s").withZone(ZoneId.systemDefault()).format(Instant.now()))));
        networkInterception.startNetworkInterception();
    }

    public  void attach_interception(Scenario scenario){
        networkInterception.getHarAsBytes().ifPresent(harBytes -> {
            scenario.attach(harBytes,"application",String.format("%s-%s.har",scenario.getName(),(DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-mm-ss-s").withZone(ZoneId.systemDefault()).format(Instant.now()))));
        });
    }
}
