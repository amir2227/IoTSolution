package com.shd.cloud.iot.payload.request;

public class SensorHistoryRequest {

    private String data;
    private String token;

    public SensorHistoryRequest() {
    }

    public SensorHistoryRequest(String data, String token) {
        this.data = data;
        this.token = token;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
