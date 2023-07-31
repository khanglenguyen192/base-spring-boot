package com.example.base.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseException extends Exception {
    protected String errorCode;
    protected String errorDesciption;
    protected String errorTime;
}
