package com.shd.cloud.iot.payload.request;

import javax.validation.constraints.Min;

public class SearchRequest {
    private String key;
    @Min(13)
    private Long startDate;
    @Min(13)
    private Long endDate;

    public SearchRequest() {
    }

    public SearchRequest(String key, Long startDate, Long endDate) {
        this.key = key;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

}
