package workspace.application.domain.core;

import io.cucumber.spring.ScenarioScope;
import jakarta.annotation.PostConstruct;
import lombok.extern.java.Log;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
@Configuration
@ScenarioScope
@Log
public class PageActions {

    @Autowired
    WebdriverFactory webdriverFactory;

    @Value("${default.explicit.wait}")
    private long DEFAULT_TIMEOUT;

    @Value("${default.pageload.wait}")
    private long DEFAULT_PAGELOAD_TIMEOUT;

    private FluentWait<WebDriver> wait;

    @PostConstruct
    private void initializePageActions(){
        wait = new FluentWait<>(webdriverFactory.getDriver());
        wait.ignoreAll(
                List.of(
                        NoSuchElementException.class,
                        TimeoutException.class,
                        ElementNotInteractableException.class,
                        ElementClickInterceptedException.class,
                        NotFoundException.class)).pollingEvery(Duration.ofMillis(500));
    }


    protected synchronized void navigateTo(String URL){
        webdriverFactory.getDriver().get(URL);
        log.info(String.format("Navigated to the URL -> %s",URL));
    }

    protected synchronized void click(By locator){
        this.wait_for_element_to_exist(locator).click();
        log.info(String.format("Element located by %s is clicked",locator));
    }

    protected synchronized void enterText(By locator, String text){
        this.wait_for_element_to_exist(locator).sendKeys(text);
        log.info(String.format("Text \"%s\" entered in the element located by -> %s",text,locator));
    }

    protected synchronized void wait_for_pageload(){
        wait.withMessage("PageLoad timeout exceeded").withTimeout(Duration.ofSeconds(DEFAULT_PAGELOAD_TIMEOUT)).until(webDriver -> {
            return  getJavascriptExecutor(webDriver).executeScript("return document.readyState === 'complete'");
        });
        log.info("page-load completed");
    }

    protected synchronized WebElement wait_for_element_to_exist(long timeout, By locator){
        wait_for_pageload();
        var element = wait.withTimeout(Duration.ofSeconds(timeout))
                .withMessage(String.format("Element located by -> \"%s\" is not found",locator))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
        if(element!=null){
            scroll_to_the_element(element);
            log.info("Element found and returned -> "+locator);
        }
        return element;
    }

    protected synchronized WebElement wait_for_element_to_exist(By locator){
        wait_for_pageload();
        var element = wait.withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .withMessage(String.format("Element located by -> \"%s\" is not found",locator))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
        if(element!=null)
            scroll_to_the_element(element);
        return element;
    }

    protected synchronized List<WebElement> wait_for_elements_to_exist(long timeout, By locator){
        wait_for_pageload();
        var element = wait.withTimeout(Duration.ofSeconds(timeout))
                .withMessage(String.format("Element located by -> \"%s\" is not found",locator))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        return element;
    }

    protected synchronized JavascriptExecutor getJavascriptExecutor(WebDriver webDriver){
        return (JavascriptExecutor) webDriver;
    }

    protected synchronized void scroll_to_the_element(WebElement element){
        getJavascriptExecutor(webdriverFactory.getDriver()).executeScript("arguments[0].scrollIntoView(false)",element);
        log.info("successfully scrolled to the element");
    }

    protected synchronized boolean verify_element_present(By locator){
        boolean result = false;
        try{
            result = wait_for_element_to_exist(10,locator) != null;
        }
        catch (Exception e){

        }
        return result;
    }

    protected synchronized boolean verify_elements_present(By locator){
        boolean result = false;
        try{
            result = !wait_for_elements_to_exist(10,locator).isEmpty();
        }
        catch (Exception e){

        }
        return result;
    }

}
