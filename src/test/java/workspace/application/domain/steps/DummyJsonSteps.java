package workspace.application.domain.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.restassured.response.Response;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import workspace.application.domain.actions.DummyJson_Action;
import workspace.application.domain.actions.ScenarioContext;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Map;
import java.util.Optional;

@Log
public class DummyJsonSteps {

    @Autowired
    DummyJson_Action dummyJsonAction;

    @Autowired
    ScenarioContext scenarioContext;

    @Given("Add {string} as a new product")
    public void exe_add_new_product(String productName){
        dummyJsonAction.add_new_product(productName);
    }

    @Given("Get product with Id {int} from product catalog")
    public void get_product_with_id(int id){
        dummyJsonAction.getProductWithId(id);
    }

    @And("verify product {string} is returned from catalog")
    public void verify_product_with_id(String product){
        Optional.ofNullable((Response)scenarioContext.get("response")).ifPresentOrElse(response -> {
            assertThat(Optional.ofNullable(response.getBody().jsonPath().getString("title")).get()).isEqualTo(product);
        }, ()-> Assertions.fail("Response does not exists in context"));
    }
}
