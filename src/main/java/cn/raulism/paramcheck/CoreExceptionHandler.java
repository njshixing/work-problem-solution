package cn.raulism.paramcheck;

import cn.raulism.common.WrapperResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CoreExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public WrapperResponse errorHandler(Exception e) {
        return WrapperResponse.fail(e.getMessage());
    }
}
