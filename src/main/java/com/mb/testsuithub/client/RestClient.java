package com.mb.testsuithub.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


import com.mb.testsuithub.server.TestSuitLauncher;
import org.apache.commons.lang3.StringUtils;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

//package com.daimler.testbenchgrid.node.dutrestclient;
public class RestClient implements TestSuitProperties {
    private static final Logger LOGGER = Logger.getLogger(RestClient.class.getName());
    //private String clientIp;
    //private String drivingSession;
    private String dutBaseUrl;

    private OkHttpClient httpClient;

    public RestClient() {

    }
    public RestClient(String baseUrl) {
        this.dutBaseUrl = baseUrl;
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().readTimeout(240, TimeUnit.SECONDS).writeTimeout(300, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS);
        this.httpClient = clientBuilder.build();
    }
/*
    public RestClient(String clientIp, String baseUrl, String drivingSession) {
        this.clientIp = clientIp;
        this.dutBaseUrl = baseUrl;
        this.drivingSession = drivingSession;

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().readTimeout(240, TimeUnit.SECONDS).writeTimeout(300, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS);
        this.httpClient = clientBuilder.build();
    }

    public ResponseData startSession(String channels, String body) throws IOException {
        okhttp3.HttpUrl.Builder builder = HttpUrl.parse(dutBaseUrl).newBuilder();
        builder.addQueryParameter("sessionId", drivingSession);

        if (!StringUtils.isBlank(channels)) {
            for (String channel : channels.split(",")) {
                builder.addQueryParameter("channel", channel);
            }
        }
        HttpUrl endpoint = builder.build();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        Map<String, String> headers = new HashMap<>();
        if (!StringUtils.isBlank(clientIp)) {
            headers.put("X-Client-IP", clientIp);
        }
        RequestBody requestBody = RequestBody.create(mediaType, body);
        return sendRequest(endpoint.url().toString(), headers, requestBody, "POST");
    }

    public ResponseData endSession() throws IOException {
        String endpoint = dutBaseUrl;
        HttpUrl endpointUrl = HttpUrl.parse(endpoint).newBuilder().addQueryParameter("sessionId", drivingSession).build();
        return sendRequest(endpointUrl.url().toString(), new HashMap<>(), null, "DELETE");
    }

    public ResponseData getChannels() throws IOException {
        String endpoint = dutBaseUrl + "channels";
        return sendRequest(endpoint, new HashMap<>(), null, "GET");
    }

    public ResponseData healthStatus() throws IOException {
        String endpoint = dutBaseUrl + "healthcheck";
        return sendRequest(endpoint, new HashMap<>(), null, "GET");
    }

    */

    public ResponseData sendRequest(String endpoint, Map<String, String> headers, RequestBody body, String requestMethod) throws IOException {
        Builder requestBuilder = new Request.Builder();
        headers.put("X-Node-Version", TestSuitLauncher.FIXED_VERSION);
        for (Entry<String, String> entry : headers.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        ResponseData responseData = new ResponseData();
        Request request = requestBuilder.url(this.dutBaseUrl + endpoint).method(requestMethod, body).build();
        LOGGER.info(request.toString());
        try (Response response = httpClient.newCall(request).execute()) {
            responseData.setResponseBody(response.body().string());
            responseData.setResponseCode(response.code());
            LOGGER.info(responseData.toString());
            return responseData;
        }
    }


}

