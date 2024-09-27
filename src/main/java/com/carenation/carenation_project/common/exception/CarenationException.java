package com.carenation.carenation_project.common.exception;

import com.carenation.carenation_project.common.response.ResultCode;

import lombok.Getter;

@Getter
public class CarenationException extends RuntimeException{
    private ResultCode resultCode;

    public CarenationException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }
}
