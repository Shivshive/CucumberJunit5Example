package workspace.application.domain.reporter;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.Reportable;
import net.masterthought.cucumber.presentation.PresentationMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class CucumberReporter {

    private static String PROJECT_NAME;
    private static String BROWSER_TYPE;

    @Value("${browser.type}")
    private void setBrowserType(String browserType) {
        BROWSER_TYPE = browserType;
    }

    @Value("${project.title}")
    private void setProjectName(String projectName) {
        PROJECT_NAME = projectName;
    }

    public static void setupReport() {

        File reportOutputDirectory = new File(String.format("Cucumber-Reports//%s", DateTimeFormatter.ofPattern("dd-MM-yyyy-mm-ss-s").withZone(ZoneId.systemDefault()).format(Instant.now())));
        List<String> jsonFiles = new ArrayList<>();
        jsonFiles.add("target/cucumber-report/cucumber.json");

        Configuration configuration = new Configuration(reportOutputDirectory, PROJECT_NAME);
        configuration.addClassifications("Platform", System.getProperty("os.name"));
        configuration.addClassifications("OS Version", System.getProperty("os.version"));
        configuration.addClassifications("OS Architecture", System.getProperty("os.arch"));
        configuration.addClassifications("Browser", BROWSER_TYPE);
        configuration.addClassifications("Branch", "release/1.0");
        configuration.addPresentationModes(PresentationMode.PARALLEL_TESTING);
        configuration.setQualifier("cucumber-report-1", "First report");
        configuration.setQualifier("cucumber-report-2", "Second report");

        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        Reportable result = reportBuilder.generateReports();
    }
}
