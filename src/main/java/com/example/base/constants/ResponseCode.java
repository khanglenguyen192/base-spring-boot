package com.example.base.constants;

public enum ResponseCode {
    FAILED("500", "FAILED"),
    SUCCESSFUL("200", "SUCCESS"),
    API_FAILED("444", "Call API failed");
    private final String code;
    private final String description;

    ResponseCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() { return this.code; }

    public String getDescription() { return this.description; }
}
