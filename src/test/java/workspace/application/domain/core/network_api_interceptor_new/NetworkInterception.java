package workspace.application.domain.core.network_api_interceptor_new;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.sstoehr.harreader.model.*;
import io.cucumber.spring.ScenarioScope;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.NetworkInterceptor;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.http.Filter;
import org.openqa.selenium.remote.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import workspace.application.domain.core.WebdriverFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ScenarioScope
@Slf4j
public class NetworkInterception {

    private DevTools devTools;
    private String hostUrl;
    private WebDriver driver;

    @Setter
    private String harfileName;

    private HarCreatorBrowser harCreatorBrowser;

    @Autowired
    WebdriverFactory webdriverFactory;

    private ConcurrentHashMap<HarTimeFrame, HarMap> harMap = new ConcurrentHashMap<>();

    public NetworkInterception() {

    }

    @PostConstruct
    public void init() {
        this.driver = webdriverFactory.getDriver();
    }


    public void startNetworkInterception() {

        devTools = getCDPSession();
        devTools.createSession();

        System.out.println(devTools.getCdpSession().toString());
        System.out.println(((RemoteWebDriver) driver).getSessionId().toString());

        harCreatorBrowser = getHarCreatorBrowser();

        Filter filter = (Filter) next -> {
            return (req) -> {
                Long startTime = Calendar.getInstance().getTimeInMillis();
                HttpResponse res = next.execute(req);
                Long endTime = Calendar.getInstance().getTimeInMillis();

                HarTimeFrame harTimeFrame = new HarTimeFrame();
                harTimeFrame.setStartTime(startTime);
                harTimeFrame.setEndTime(endTime);
                harMap.put(harTimeFrame,new HarMap(req,res,harTimeFrame));

                return res;
            };
        };
        NetworkInterceptor networkInterceptor = new NetworkInterceptor(this.driver, filter);
    }

    private Har createHar() {

        Har har = new Har();
        HarLog harLog = new HarLog();
        HarPage harPage = new HarPage();

        List<HarPage> harPages = new ArrayList<>();
        List<HarEntry> harEntries = new LinkedList<>();

        harMap.forEach((k, v) -> {
            harEntries.add(convertToHarEntry(v));
        });

        harLog.setCreator(harCreatorBrowser);
        harLog.setBrowser(harCreatorBrowser);

        harLog.setPages(harPages);
        harLog.setEntries(harEntries);

        har.setLog(harLog);

        return har;
    }

    private DevTools getCDPSession() {
        if (this.driver instanceof RemoteWebDriver) {
            RemoteWebDriver driver_ = (RemoteWebDriver) new Augmenter().augment(this.driver);
            return ((HasDevTools) driver_).getDevTools();
        }
        return ((HasDevTools) this.driver).getDevTools();
    }

    private HarCreatorBrowser getHarCreatorBrowser() {

        String browserName = (String) Optional.ofNullable(getCapability(driver, "browserName")).orElse("QA-Browser");
        String browserVersion = (String) Optional.ofNullable(getCapability(driver, "browserVersion")).orElse("0.0.1");

        HarCreatorBrowser harCreatorBrowser = new HarCreatorBrowser();
        harCreatorBrowser.setName(browserName);
        harCreatorBrowser.setVersion(browserVersion);

        System.out.println(String.format("creating har browser %S - version %s",browserName, browserVersion));
        return harCreatorBrowser;
    }

    private <T> T getCapability(WebDriver driver, String capabilityName) {
        T value = null;
        try {
            Field capabilityField = RemoteWebDriver.class.getDeclaredField("capabilities");
            capabilityField.setAccessible(true);
            Capabilities capabilities = (Capabilities) capabilityField.get(driver);
            value = (T) capabilities.getCapability(capabilityName);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return value;
    }



    private HarEntry convertToHarEntry(HarMap harMap) {
        return (new HarEntryConverter(harMap.getHttpRequest(), harMap.getHttpResponse(), harMap.getTimeFrame()))
                .convertToHarEntry();
    }

    public Optional<Path> createHarFile() {
        Har har = createHar();
        try {
            ObjectMapper mapper = new ObjectMapper();
            String harFileData = mapper.writeValueAsString(har);
            if(!Files.exists(Paths.get("harFiles"))){
                Files.createDirectory(Paths.get("harFiles"));
            }
            Files.write(Paths.get("harFiles",this.harfileName), harFileData.getBytes());
            log.info(String.format("Har File -> %s  created successfully ... ",harfileName));
            return Optional.of(Paths.get("harFiles",this.harfileName));
        } catch (Exception e) {
            log.info("======== xxxx [ Unable to create har file ] xxxx ================ ");
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<byte[]> getHarAsBytes(){
        Har har = createHar();
        ObjectMapper mapper = new ObjectMapper();
        try {
            return Optional.ofNullable(mapper.writeValueAsBytes(har));
        } catch (JsonProcessingException e) {
            log.error(String.format("Creation of Har Bytes Failed with erro ==> %s", e.getMessage()));
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
