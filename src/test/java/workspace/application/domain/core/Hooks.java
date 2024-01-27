package workspace.application.domain.core;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.extern.java.Log;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.annotation.AfterTestExecution;
import org.springframework.test.context.event.annotation.BeforeTestExecution;

@Log
public class Hooks {

    @Autowired
    WebdriverFactory webdriverFactory;

    @Before("@ui")
    public void beforeScenario(Scenario scenario){
        log.info("Before scenario hook started");
        webdriverFactory.initializeDriver();
        log.info("Before scenario hook executed");
    }

    @After("@ui")
    public void afterScenario(Scenario scenario){
        log.info("After scenario hook started");
        webdriverFactory.removeDriver();
        log.info("After scenario hook executed");

    }
}
