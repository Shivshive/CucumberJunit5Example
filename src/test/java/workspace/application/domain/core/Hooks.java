package workspace.application.domain.core;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;

@Log
public class Hooks {

    @Autowired
    WebdriverFactory webdriverFactory;

    @Before("@ui")
    public void beforeScenario(Scenario scenario){
        webdriverFactory.initializeDriver();
        log.info("Before scenario hook executed");
    }

    @After("@ui")
    public void afterScenario(Scenario scenario){
        webdriverFactory.removeDriver();
        log.info("After scenario hook executed");

    }
}
