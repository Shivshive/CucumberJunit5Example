package workspace.application.runner;

import lombok.extern.java.Log;
import org.junit.platform.suite.api.*;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import static io.cucumber.junit.platform.engine.Constants.*;

@Log
@Suite
@IncludeEngines({"cucumber"})
// if we are not defining feature file path in the @ConfigurationParameter then
// we can enable this annotation (@SelectDirectories) to specify from where to pick the feature files
//@SelectDirectories("src/test/java/cj/workspace/domain")

// If we are planning to take feature files from src/test/resources then we can also use @SelectClasspathResource("foldername"), where foldername is the name of the folder inside resources folder where
// feature files are present.
@ConfigurationParameters(value = {
        @ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "workspace.application.domain"), // define steps files path
//        @ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@ui"),
        @ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty,summary,html:target/cucumber.html,json:target/cucumber-report/cucumber.json"), // define reporting and console output
        @ConfigurationParameter(key = FEATURES_PROPERTY_NAME, value = "src/test/java/workspace/application/domain"), // define feature files path,
        @ConfigurationParameter(key = PARALLEL_EXECUTION_ENABLED_PROPERTY_NAME , value = "true"), // define parallel execution true/false,
        @ConfigurationParameter(key = PARALLEL_CONFIG_STRATEGY_PROPERTY_NAME, value = "fixed"), // define parallel strategy fixed/dynamic/custom
        @ConfigurationParameter(key = PARALLEL_CONFIG_FIXED_PARALLELISM_PROPERTY_NAME, value = "5"), // define fixed count -- only applicable for fixed strategy,
})
public class SuiteRunnerTest {

    @BeforeTestClass
    public void beforeTestClass(){
        log.info("beforeTestClass started");
    }

}

// instead of the @ConfigurationParameters, we can also define junit-platform.properties. In that case we do not need @ConfigurationParamters or @ConfigurationParamter.
// we just need to put junit-platform.properties in the src/test/resources folder with the below values those can be found on below link
// https://github.com/cucumber/cucumber-jvm/tree/main/cucumber-junit-platform-engine

// If we want to add these cucumber properties in the suite class itself instead of defining them in the junit-platform.properties file then the constant values can be found here -
// https://github.com/cucumber/cucumber-jvm/blob/main/cucumber-junit-platform-engine/src/main/java/io/cucumber/junit/platform/engine/Constants.java

// This link can help understand the ways in which junit configuration can be added - https://junit.org/junit5/docs/current/user-guide/#running-tests-config-params

// Maven command to run this mvn clean test -Dsurefire.includeJunit5Engines=cucumber