package com.carenation.carenation_project.common.response;

import lombok.Getter;

@Getter
class Header {
    private String resultCode;
    private String resultMessage;

    Header(ResultCode resultCode) {
        this.resultCode = resultCode.getCode();
        this.resultMessage = resultCode.getMessage();
    }

    public static Header success() {
        return new Header(ResultCode.SUCCESS);
    }
}

