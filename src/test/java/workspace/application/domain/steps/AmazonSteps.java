package workspace.application.domain.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import workspace.application.domain.actions.AmazonAction;

@Log
public class AmazonSteps {

    @Autowired
    AmazonAction amazonAction;

    public AmazonSteps() {
        System.out.println("Constructor of AmazonSteps is called.");
    }

    @Given("open amazon india website")
    public void open_amazon_website(){
        amazonAction.open_amazon_website();
    }

    @And("search with product {string}")
    public void search_with_product(String productName){
        amazonAction.search_with_productname(productName);
    }
}
