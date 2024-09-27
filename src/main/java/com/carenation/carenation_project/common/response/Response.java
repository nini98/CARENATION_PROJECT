package com.carenation.carenation_project.common.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Response<T> {

    private Header header;
    private T data;

    public static <T> Response<T> success() {
        return success(null);
    }

    public static <T> Response<T> success(T data) {
        return Response.<T>builder()
            .header(Header.success())
            .data(data)
            .build();
    }

    public static <T> Response<T> fail(ResultCode resultCode) {
        return fail(resultCode, null);
    }

    public static <T> Response<T> fail(ResultCode resultCode, T data) {
        return Response.<T>builder()
            .header(new Header(resultCode))
            .data(data)
            .build();
    }
}

