# Cucumber Junit-5 Framework

### Suite Class
```java
@Suite
@IncludeEngines("cucumber")
@ConfigurationParameters(value = {
        @ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "cj.workspace.domain"), // define steps files path
        @ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@Vegie or @fruit"),
        @ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty,summary,html:target/cucumber.html,json:target/cucumber.json"), // define reporting and console output
        @ConfigurationParameter(key = FEATURES_PROPERTY_NAME, value = "src/test/java/cj/workspace/domain"), // define feature files path
})
public class SuiteRunnerTest {
}
```

OR

```java
import org.springframework.beans.factory.annotation.Value;

public class CustomLauncherTest {

    @Value("${parallel.thread.count}")
    private String threadCount;

    @BeforeAll
    public static void beforeAll() {
        log.info(" *************************** [[ before all executed ]] ******************************** ");
    }

    @Test
    public void runTest() {

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .filters(EngineFilter.includeEngines("cucumber"))
                .configurationParameters(Map.of(
                        GLUE_PROPERTY_NAME, "workspace.application.domain",
                        PLUGIN_PROPERTY_NAME, "pretty,summary,html:target/cucumber.html,json:target/cucumber-report/cucumber.json",
                        FEATURES_PROPERTY_NAME, "src/test/java/workspace/application/domain",
                        PARALLEL_EXECUTION_ENABLED_PROPERTY_NAME, "true",
                        PARALLEL_CONFIG_STRATEGY_PROPERTY_NAME, "fixed",
                        PARALLEL_CONFIG_FIXED_PARALLELISM_PROPERTY_NAME, threadCount
                ))
                .build();
        Launcher launcher = LauncherFactory.create();
        launcher.execute(request);

    }

    @AfterAll
    public static void afterAll() {
        log.info(" *************************** [[ after all executed ]] ******************************** ");
    }
}

```

#### Implemented Spring boot variable in CustomeLauncher Class which is taking property value from application.properties

```properties

parallel.thread.count=5

```
---

### To run Test with different configuration make use of spring profiles

> --spring.profiles.active=dev

Note: make sure to create files with suffix with the profile name like below
* application-<profilename>.properties

- Example: application-dev.properties
---
### To run it via Maven CLI use 
> mvn clean test -Dsurefire.includeJunit5Engines=cucumber

It will run the suite file tagged with @Suite from the classpath.

---
### To run via intellj Junit Configuration
* Add Junit Configuration
* Select Class runner.SuiteRunnerTest
* To Add Tags to run pass -Dcucumber.filter.tags=""@Test"
>  Note: can also pass -Dcucumber.filter.tags in maven cli command.
--- 
### useful links
* [Github Cucumber Junit5](https://junit.org/junit5/docs/current/user-guide/#running-tests-config-params)
* [Github Cucumber @ConfigurationParameter Constants](https://github.com/cucumber/cucumber-jvm/blob/main/cucumber-junit-platform-engine/src/main/java/io/cucumber/junit/platform/engine/Constants.java)

---

### Supply configuration through junit-platform.properties
If you want to specify configuration in junit-platform.properties file then use below template
```properties
cucumber.ansi-colors.disabled=true
cucumber.features=src/test/java/cj/workspace/domain
cucumber.filter.tags=@fruit
cucumber.glue=cj.workspace.domain.steps
cucumber.plugin=pretty,summary,html:target/cucumber.html,json:target/cucumber.json
cucumber.publish.quiet=false
cucumber.execution.dry-run=false
cucumber.execution.parallel.enabled=true
cucumber.execution.parallel.config.strategy=fixed
cucumber.execution.parallel.config.fixed.parallelism=10
```
**Note :**
- Name this file as junit-platform.properties anc put this in src/test/resources folder
- Remove @ConfigurationParamters from Suite if you are giving junit-platform.properties file. 

---

### Network Interception is added -- 
#### Dynamically generate Har file and attach to the cucumber report
```java

public void start_interception(Scenario scenario){
    networkInterception.setHarfileName(String.format("%s-%s.har",scenario.getName(),(DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-mm-ss-s").withZone(ZoneId.systemDefault()).format(Instant.now()))));
    networkInterception.startNetworkInterception();
}

public  void attach_interception(Scenario scenario){
    networkInterception.getHarAsBytes().ifPresent(harBytes -> {
        scenario.attach(harBytes,"application",String.format("%s-%s.har",scenario.getName(),(DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-mm-ss-s").withZone(ZoneId.systemDefault()).format(Instant.now()))));
    });
}

```

##### Future upcoming Enhancements  ...
- [x] Selenium Webdriver integration
- [x] Cucumber Reporter integration
- [x] Cucumber-Spring integration
- [x] Added Custom Launcher
- [x] Added network interception logic to intercept network traffic and attach as a har file in report
- [x] Added profiles 
- [x] Taking values from application context in CustomLauncher via spring @Value tag
- [ ] Re-run functionality integration
