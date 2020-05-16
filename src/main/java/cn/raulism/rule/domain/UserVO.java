package cn.raulism.rule.domain;

import lombok.Data;

@Data
public class UserVO {
    private String name;
    private String sex;
    private String password;
    private String email;
    private String mobile;
}
