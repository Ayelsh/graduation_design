package com.ykj.graduation_design.common;


import lombok.Generated;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

@Getter
@Generated
@ToString
public class RestResult<T> {
    private final Integer code;
    private final String msg;
    private final T data;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public static final RestResult<?> OK = new RestResult<>(HttpStatus.OK);
    public static final RestResult<?> FAIL = new RestResult<>(HttpStatus.INTERNAL_SERVER_ERROR);

    public RestResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public RestResult(Integer code, String msg) {
        this(code, msg, null);
    }

    public RestResult(HttpStatus httpStatus) {
        this(httpStatus.value(), httpStatus.getReasonPhrase());
    }

    public RestResult(HttpStatus httpStatus, T data) {
        this(httpStatus.value(), httpStatus.getReasonPhrase(), data);
    }
}
