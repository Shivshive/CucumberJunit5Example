package workspace.application.domain.actions;

import io.cucumber.spring.ScenarioScope;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import workspace.application.domain.TestConfig.CommonWorld;
import workspace.application.domain.core.RestSpecificationFactory;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

@Component
@ScenarioScope
@Slf4j
public class CommonAPIAction {

    @Autowired
    RestSpecificationFactory restSpecificationFactory;

    @Autowired
    ScenarioContext scenarioContext;

    @Autowired
    Environment environment;

    public CommonAPIAction() {
    }


    public Optional<String> getAuthToken() {

        Optional<String> token = Optional.empty();

        String AUTH_TOKEN_URL = environment.getProperty("auth.token.url");
        String USERNAME = environment.getProperty("auth.username");
        String PASSWORD = environment.getProperty("auth.password");

        String payload = restSpecificationFactory.prepareRequestFromTemplate(
                Paths.get(CommonWorld.commonConfig.getProperty("auth.request.template"))
                , Map.of("username", USERNAME, "password", PASSWORD)).orElse("""
                  {
                    "username": "kminchelle",
                    "password": "0lelplR",
                  }
                """);
        token = Optional.ofNullable(restSpecificationFactory.getInstance().body(payload)
                .header("Content-Type", "application/json")
                .when()
                .post(AUTH_TOKEN_URL)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response().jsonPath().getString("token"));

        return token;
    }

    public void get_auth_user() {

        String AUTH_USER_URL = environment.getProperty("auth.user.url");

        Response response = restSpecificationFactory.getInstance()
                .header("Authorization", String.format("Bearer %s", getAuthToken().get()))
                .when()
                .get(AUTH_USER_URL)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();

        log.info(String.format("Auth User %s is being fetched.", response.getBody().jsonPath().getString("username")));
        scenarioContext.put("response", response);
    }
}
