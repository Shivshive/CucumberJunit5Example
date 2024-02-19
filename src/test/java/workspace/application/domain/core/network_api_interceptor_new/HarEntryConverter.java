package workspace.application.domain.core.network_api_interceptor_new;

import de.sstoehr.harreader.model.*;
import org.openqa.selenium.remote.http.HttpRequest;
import org.openqa.selenium.remote.http.HttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HarEntryConverter {

    private HttpRequest request;
    private HttpResponse response;
    private HarTimeFrame time;

    public HarEntryConverter(HttpRequest request, HttpResponse response, HarTimeFrame timeFrame) {
        this.request = request;
        this.response = response;
        this.time = timeFrame;
    }


    public HarEntry convertToHarEntry() {

        HarEntry harEntry = new HarEntry();

        harEntry.setRequest(getHarRequest());
        harEntry.setResponse(getHarResponse());
        harEntry.setTime((int) (this.time.getEndTime() - this.time.getStartTime()));
        harEntry.setStartedDateTime(new Date(this.time.getStartTime()));
        harEntry.setTimings(getHarTiming());
        harEntry.setPageref("Page");

        return harEntry;
    }

    private HarTiming getHarTiming() {
        HarTiming harTiming = new HarTiming();
        harTiming.setBlocked(0);
        harTiming.setDns(0);
        harTiming.setConnect(0);
        harTiming.setSend(0);
        harTiming.setWait(0);
        harTiming.setReceive(0);
        return harTiming;
    }

    private HarRequest getHarRequest() {

        HarRequest harRequest = new HarRequest();
        harRequest.setHttpVersion("HTTP/1.1");
        harRequest.setUrl(this.request.getUri());
        harRequest.setMethod(HttpMethod.valueOf(this.request.getMethod().name()));
        harRequest.setHeaders(getHarRequestHeaders());
        try {
            harRequest.setBodySize((long) this.request.getContent().get().available());
        } catch (IOException e) {
            harRequest.setBodySize(0L);
        }
        harRequest.setPostData(getRequestPostData());
        return harRequest;
    }

    private HarResponse getHarResponse() {

        HarResponse harResponse = new HarResponse();
        harResponse.setStatus(this.response.getStatus());
//        harResponse.setStatusText(this.response.isSuccessful() ? "OK" : "FAILED");
        harResponse.setStatusText(getResponseStatus().name());
        harResponse.setHttpVersion("HTTP/1.1");
        harResponse.setRedirectURL("");
        harResponse.setHeaders(getHarResponseHeaders());
        harResponse.setContent(getResponseContent());
        return harResponse;
    }


    private List<HarHeader> getHarRequestHeaders() {

        List<HarHeader> harHeaders = new ArrayList<>();

        this.request.getHeaderNames().forEach(headerName -> {
            HarHeader harHeader = new HarHeader();
            harHeader.setName(headerName);
            harHeader.setValue(this.request.getHeader(headerName));
            harHeaders.add(harHeader);
        });

        return harHeaders;
    }

    private List<HarHeader> getHarResponseHeaders() {

        List<HarHeader> harHeaders = new ArrayList<>();
        this.response.getHeaderNames().forEach(headerName -> {
            HarHeader harHeader = new HarHeader();
            harHeader.setName(headerName);
            harHeader.setValue(this.request.getHeader(headerName));
            harHeaders.add(harHeader);
        });
        return harHeaders;
    }

    private HarPostData getRequestPostData() {
        HarPostData postData = new HarPostData();
        postData.setComment("");
        postData.setMimeType(getRequestMIMETYPE());
        postData.setText(convertInputstreamToString(this.request.getContent().get()));
        return postData;
    }

    private String getRequestMIMETYPE() {
        return Optional.ofNullable(this.request.getHeader("Content-Type")).orElse("").equalsIgnoreCase("")
                ? "application/x-www-form-urlencoded"
                : this.request.getHeader("Content-Type");
    }

    private String getResponseMIMETYPE() {
        return Optional.ofNullable(this.response.getHeader("Content-Type")).orElse("").equalsIgnoreCase("")
                ? "application/x-www-form-urlencoded"
                : this.request.getHeader("Content-Type");
    }

    private String convertInputstreamToString(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        try {

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            byte[] byteArray = buffer.toByteArray();

            sb.append(new String(byteArray, StandardCharsets.UTF_8));

        } catch (Exception e) {

        }
        return sb.toString();
    }

    private HttpStatus getResponseStatus() {
        return HttpStatus.byCode(this.response.getStatus());
    }

    private long getResponseBodySize() {
        long size = 0L;

        try {
            size = (long) this.response.getContent().get().available();
        } catch (Exception e) {

        }
        return size;
    }

    private HarContent getResponseContent() {
        HarContent harContent = new HarContent();
        harContent.setSize(getResponseBodySize());
        harContent.setMimeType(getResponseMIMETYPE());
        harContent.setText(convertInputstreamToString(this.response.getContent().get()));
        return harContent;
    }

}
