package runner;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@IncludeEngines("cucumber")
// if we are not defining feature file path in the @ConfigurationParameter then
// we can enable this annotation (@SelectDirectories) to specify from where to pick the feature files
//@SelectDirectories("src/test/java/cj/workspace/domain")

// If we are planning to take feature files from src/test/resources then we can also use @SelectClasspathResource("foldername"), where foldername is the name of the folder inside resources folder where
// feature files are present.
@ConfigurationParameters(value = {
        @ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "cj.workspace.domain"), // define steps files path
        @ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@Vegie or @fruit"),
        @ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty,summary,html:target/cucumber.html,json:target/cucumber.json"), // define reporting and console output
        @ConfigurationParameter(key = FEATURES_PROPERTY_NAME, value = "src/test/java/cj/workspace/domain"), // define feature files path
})
public class SuiteRunnerTest {
}

// instead of the @ConfigurationParameters, we can also define junit-platform.properties. In that case we do not need @ConfigurationParamters or @ConfigurationParamter.
// we just need to put junit-platform.properties in the src/test/resources folder with the below values those can be found on below link
// https://github.com/cucumber/cucumber-jvm/tree/main/cucumber-junit-platform-engine

// If we want to add these cucumber properties in the suite class itself instead of defining them in the junit-platform.properties file then the constant values can be found here -
// https://github.com/cucumber/cucumber-jvm/blob/main/cucumber-junit-platform-engine/src/main/java/io/cucumber/junit/platform/engine/Constants.java

// This link can help understand the ways in which junit configuration can be added - https://junit.org/junit5/docs/current/user-guide/#running-tests-config-params

// Maven command to run this mvn clean test -Dsurefire.includeJunit5Engines=cucumber