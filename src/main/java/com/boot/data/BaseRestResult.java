package com.boot.data;

import java.util.Map;

public class BaseRestResult {
    private Map<String,Object> resultMap;
    public BaseRestResult(Map<String, Object> resultMap) {
        this.resultMap=resultMap;
    }

    @Override
    public String toString() {
        return "BaseRestResult{" +
                "resultMap=" + resultMap +
                '}';
    }
}
