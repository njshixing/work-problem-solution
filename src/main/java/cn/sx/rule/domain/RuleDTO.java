package cn.sx.rule.domain;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

public class RuleDTO {

    @NotBlank(message = "id不能为空")
    private String id;

    @Max(value = 10, message = "name最大为10")
    private String name;

    private String desc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
