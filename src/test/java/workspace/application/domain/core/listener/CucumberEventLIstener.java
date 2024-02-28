package workspace.application.domain.core.listener;

import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestRunFinished;
import lombok.extern.slf4j.Slf4j;
import workspace.application.domain.TestConfig.CommonWorld;
import workspace.application.domain.reporter.CucumberReporter;

@Slf4j
public class CucumberEventLIstener implements EventListener {
    @Override
    public void setEventPublisher(EventPublisher publisher) {

        publisher.registerHandlerFor(TestRunFinished.class, event -> {
            log.info("TestRunFinished is called");
            CucumberReporter.setupReport();
        });
    }
}
