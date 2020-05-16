package cn.sx.common;

import lombok.Data;

@Data
public class WrapperResponse {
    public int code;
    public String message;
    public Object data;

    public WrapperResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public WrapperResponse(String message, Object data) {
        this.data = data;
        this.message = message;
    }

    public WrapperResponse(int code, String message, Object data) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public WrapperResponse() {
        this.code = 200;
        this.message = "success";
    }

    public static WrapperResponse fail(String message) {
        return new WrapperResponse(101, message);
    }

    public static WrapperResponse success(String message, Object data) {
        return new WrapperResponse(200, message);
    }

    public static WrapperResponse success(Object data) {
        return new WrapperResponse(200, "success");
    }
}
