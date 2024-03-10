package workspace.application.domain.steps;

import io.cucumber.java.ParameterType;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.restassured.response.Response;
import lombok.extern.java.Log;

import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import workspace.application.domain.actions.ScenarioContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Log
public class CommonAPISteps {

    @Autowired
    ScenarioContext scenarioContext;

    public CommonAPISteps() {
    }

    @And("verify response status code is {int}")
    public void verify_response_code(int code) {
        Optional.ofNullable((Response) scenarioContext.get("response")).ifPresentOrElse(response -> {
            assertEquals(code, response.statusCode());
        }, () -> fail("Response is not present in scenario context"));
    }

    @And("verify response status code is one of following {codes}")
    public void verify_response_status_code_belongs_to(List<Integer> codes){
        Optional.ofNullable((Response) scenarioContext.get("response")).ifPresentOrElse(response -> {
            Assertions.assertThatList(codes).contains(response.getStatusCode());
            log.info(String.format("response code %s matched %s of list %s", response.getStatusCode(), codes.stream().filter(c -> c == response.getStatusCode()).findFirst().get(),codes.toString()));
            ((Scenario) scenarioContext.get("scenario")).log(String.format("response code %s matched %s of list %s", response.getStatusCode(), codes.stream().filter(c -> c == response.getStatusCode()).findFirst().get(),codes.toString()));
        }, () -> fail("Response is not present in scenario context"));
    }

    @ParameterType(value = ".*",name = "codes")
    public List<Integer> codes(String statusCodes){
        return Arrays.stream(statusCodes.split(",")).map(Integer::parseInt).toList();
    }
}
