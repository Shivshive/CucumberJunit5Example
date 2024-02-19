package workspace.application.domain.steps;

import org.springframework.beans.factory.annotation.Autowired;
import workspace.application.domain.core.WebdriverFactory;
import workspace.application.domain.data.Fruit;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import lombok.extern.java.Log;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;

@Log
public class SampleSteps {

    private List<Fruit> fruits;

    @Given("there is basket")
    public void thereIsBasket() {
        fruits = new ArrayList<>();
    }

    @And("put {string}")
    public void put(String name) {
        Fruit fruit = new Fruit();
        fruit.setName(name);
        fruits.add(fruit);
    }

    @And("verify total number of fruits in the basket is {int}")
    public void verifyTotalNumberOfFruitsInTheBasketIs(int total) {
        assertEquals(total,fruits.size());
        log.info("Total number of fruits matched "+fruits.size());
    }

    @And("put fruits in the basket")
    public void putFruitsInTheBasket(List<String> names) {
        for (var fruit : names) {
            Fruit fr = new Fruit();
            fr.setName(fruit);
            fruits.add(fr);
        }
    }
}
