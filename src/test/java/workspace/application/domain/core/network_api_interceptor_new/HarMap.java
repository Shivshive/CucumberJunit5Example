package workspace.application.domain.core.network_api_interceptor_new;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.openqa.selenium.remote.http.HttpRequest;
import org.openqa.selenium.remote.http.HttpResponse;

@Data
@AllArgsConstructor
public class HarMap {
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;
    private HarTimeFrame timeFrame;
}
