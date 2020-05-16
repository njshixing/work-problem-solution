package cn.sx.paramcheck.domain;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class ParamsVo {
    @NotBlank(message = "不能为空")
    private String name;

    @NotBlank
    @Length(min = 2, max = 20, message = "不可以为空，最多20个字")
    private String desc;

    @NotNull
    @Valid  // 需要加上@Valid注解，不然不会校验到Img对象
    private List<String> imgList;

    @NotNull(message = "length不能为null")
    @Range(min = 3, max = 100, message = "长度范围3-100")
    private Integer length;
}
