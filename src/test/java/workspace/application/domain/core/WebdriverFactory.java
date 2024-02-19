package workspace.application.domain.core;

import io.cucumber.spring.ScenarioScope;
import lombok.extern.java.Log;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ScenarioScope
@Log
public class WebdriverFactory {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<WebDriver>();
    @Value("${browser.type}")
    private String browserType;
    @Value("${driver.implicit.wait}")
    private long IMPLICIT_WAIT;

    @Value("${driver.chrome.headless}")
    private boolean headless_chrome;

    public void initializeDriver(){

        var driver = switch (browserType) {
            case "chrome" -> {

                ChromeOptions options = new ChromeOptions();
                if(headless_chrome)
                    options.addArguments("--headless=new");
                yield new ChromeDriver(options);
            }
            case "firefox" -> new FirefoxDriver();
            case "edge" -> new EdgeDriver();
            default -> null;
        };

        if(driver!=null){
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));
            driverThreadLocal.set(driver);
            log.info("driver initialized and set in thread local");
        }
        else {
            throw new RuntimeException("Driver Profile is not found..");
        }
    }


    public WebDriver getDriver(){
        return driverThreadLocal.get();
    }

    public void removeDriver(){
        if(driverThreadLocal.get()!=null){
            driverThreadLocal.get().quit();
            driverThreadLocal.remove();
        }
    }
}
