package workspace.application.domain.actions;

import io.cucumber.spring.ScenarioScope;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@ScenarioScope
public class ScenarioContext {

    private Map<String,Object> scnearioContext;

    public ScenarioContext() { }

    @PostConstruct
    public void init(){
        scnearioContext = new LinkedHashMap<>();
    }

    public void put(String key, Object value){
        scnearioContext.put(key,value);
    }

    public Object get(String key){
        return scnearioContext.get(key);
    }
}
