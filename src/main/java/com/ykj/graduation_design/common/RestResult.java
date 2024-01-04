package com.ykj.graduation_design.common;


import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletResponse;
import lombok.Generated;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.PrintWriter;
import java.time.LocalDateTime;

@Getter
@Generated
@ToString
@Slf4j
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
    /**
     * Response输出Json格式
     *
     * @param response
     * @param data     返回数据
     */
    public static void responseJson(ServletResponse response, Object data) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            out = response.getWriter();
            out.println(JSON.toJSONString(data));
            out.flush();
        } catch (Exception e) {
            log.error("Response输出Json异常：" + e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
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
