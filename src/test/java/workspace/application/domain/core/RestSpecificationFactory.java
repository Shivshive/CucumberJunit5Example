package workspace.application.domain.core;

import io.cucumber.java.Scenario;
import io.cucumber.spring.ScenarioScope;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.ProxySpecification;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import workspace.application.domain.TestConfig.EnvConfig;
import workspace.application.domain.actions.ScenarioContext;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

@Component
@ScenarioScope
public class RestSpecificationFactory {

    @Autowired
    private EnvConfig envConfig;

    @Autowired
    private ScenarioContext scenarioContext;

    private final Filter filter = new Filter() {
        @Override
        public Response filter(FilterableRequestSpecification filterableRequestSpecification, FilterableResponseSpecification filterableResponseSpecification, FilterContext filterContext) {

            Response response = filterContext.next(filterableRequestSpecification,filterableResponseSpecification);

            Scenario scenario = (Scenario) scenarioContext.get("scenario");
            scenario.log(filterableRequestSpecification.getBaseUri());
            scenario.log(filterableRequestSpecification.getBody());
            scenario.log(filterableRequestSpecification.getHeaders().toString());

            scenario.log(filterableRequestSpecification.getURI());
            scenario.log(response.getContentType());
            scenario.log(response.headers().toString());
            scenario.log(response.getBody().prettyPrint());

            return response;
        }
    };

    public RequestSpecification getInstance(){
        return RestAssured.given().filter(filter);
    }

    public RequestSpecification setProxy(RequestSpecification request, String protocol, String host, int port, String username, String password){
        ProxySpecification proxySpecification = new ProxySpecification(host, port, "http");
        proxySpecification.withAuth(username,password);
        request.proxy(proxySpecification);
        return request;
    }

    public void setKeystoreAndTruststore(RequestSpecification request, File keystore_jks, String keystorePassword, File truststore_jks, String trustStorePassword){

        RestAssuredConfig restAssuredConfig = RestAssuredConfig.config();
        restAssuredConfig.getSSLConfig().keyStore(keystore_jks,keystorePassword).trustStore(truststore_jks,trustStorePassword).allowAllHostnames();
        request.config(restAssuredConfig);
    }

    public  Optional<String> prepareRequestFromTemplate(Path templatePath, Map<String,Object> dataModel){
        return Optional.ofNullable(FreemarkerEngine.fromTemplate(templatePath.toString()).build(dataModel));
    }

}
