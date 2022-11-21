package com.shd.cloud.iot.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchResponse {
    private List<?> data;
    private Integer count;
    private Integer status;
}
