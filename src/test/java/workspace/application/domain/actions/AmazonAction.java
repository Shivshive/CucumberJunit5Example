package workspace.application.domain.actions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.spring.ScenarioScope;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import workspace.application.domain.core.PageActions;
import workspace.application.domain.objectRepo.AmazonHome;

@Component
@Log
@NoArgsConstructor
@ScenarioScope
public class AmazonAction extends PageActions {

    private final static String AMAZON_URL = "https://amazon.in";

    public synchronized void open_amazon_website(){
        navigateTo(AMAZON_URL);
    }

    public synchronized void search_with_productname(String productName){
        enterText(AmazonHome.search_input,productName);
        click(AmazonHome.search_submit_btn);
        assertAll(
                String.format("%s search results are not displayed",productName),
                ()-> assertTrue(verify_element_present(AmazonHome.search_result_header_txt)),
                ()-> assertTrue(verify_elements_present(AmazonHome.search_result_items_div))
        );
        log.info(String.format("%s is searched successfully",productName));
    }
}
