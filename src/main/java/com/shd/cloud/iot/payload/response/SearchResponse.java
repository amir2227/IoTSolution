package com.shd.cloud.iot.payload.response;

import java.util.List;

public class SearchResponse {
    private List<?> result;
    private Integer count;

    public SearchResponse() {
    }

    public SearchResponse(List<?> result, Integer count) {
        this.result = result;
        this.count = count;
    }

    public List<?> getResults() {
        return result;
    }

    public void setResults(List<?> result) {
        this.result = result;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
