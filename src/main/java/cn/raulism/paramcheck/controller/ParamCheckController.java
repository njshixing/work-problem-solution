package cn.raulism.paramcheck.controller;

import cn.raulism.common.WrapperResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * spring boot code generator
 */
@RestController
@Slf4j
public class ParamCheckController {

    @GetMapping("/test")
    public WrapperResponse test(@NotBlank(message = "名称不能为空") String name) {
        return WrapperResponse.success("success");
    }
}
