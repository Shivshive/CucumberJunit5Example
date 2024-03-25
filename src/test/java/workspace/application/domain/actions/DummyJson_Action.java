package workspace.application.domain.actions;

import io.cucumber.spring.ScenarioScope;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import workspace.application.domain.TestConfig.CommonWorld;
import workspace.application.domain.TestConfig.EnvConfig;
import workspace.application.domain.core.RestSpecificationFactory;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Optional;

@Component
@ScenarioScope
@Slf4j
public class DummyJson_Action {

    @Autowired
    RestSpecificationFactory restSpecificationFactory;

    @Autowired
    ScenarioContext scenarioContext;

    @Value("${add-product.url}")
    private String ADD_PRODUCT_URL;

    @Value("${get-single-product.url}")
    private String GET_PRoDUCT_URL;

    public void add_new_product(String productName) {
        ;

        Path add_product_req_template_path = Paths.get(CommonWorld.commonConfig.getProperty("add-product.request.template"));
        Optional<String> requestPayload = restSpecificationFactory.prepareRequestFromTemplate(add_product_req_template_path, new LinkedHashMap<>() {{
            put("product", productName);
        }});

        requestPayload.ifPresentOrElse(reqPayload -> {

            Response response = restSpecificationFactory.getInstance()
                    .body(reqPayload)
                    .when()
                    .post(ADD_PRODUCT_URL)
                    .then()
                    .statusCode(anyOf(equalTo(200), equalTo(201)))
                    .extract()
                    .response();
            log.info(String.format("%s", response.getBody().prettyPrint()));
            scenarioContext.put("response", response);

        }, () -> Assertions.fail(String.format("Request Payload ==> %s", requestPayload)));
    }

    public void getProductWithId(int id) {

        Response response = restSpecificationFactory.getInstance()
                .get(String.format("%s/%s",GET_PRoDUCT_URL,id))
                .then()
                .assertThat()
                .extract()
                .response();
        log.info(String.format("Get Product With ID is Executed Successfully with response code as %s", response.getStatusCode()));
        scenarioContext.put("response", response);
    }

}
