package workspace.application.domain.objectRepo;

import org.openqa.selenium.By;

public class AmazonHome {

    public final static By search_input = By.cssSelector("input#twotabsearchtextbox");
    public final static By search_submit_btn = By.cssSelector("input#nav-search-submit-button");
    public final static By search_result_header_txt = By.xpath("//span[text()='Results']");
    public final static By search_result_items_div = By.xpath("//div[contains(@class,'s-result-item s-asin')]");

}
