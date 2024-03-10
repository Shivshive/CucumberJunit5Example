package workspace.application.domain.steps;

import io.cucumber.java.en.Given;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import workspace.application.domain.actions.DummyJson_Action;

@Log
public class DummyJsonSteps {

    @Autowired
    DummyJson_Action dummyJsonAction;

    @Given("Add {string} as a new product")
    public void exe_add_new_product(String productName){
        dummyJsonAction.add_new_product(productName);
    }
}
