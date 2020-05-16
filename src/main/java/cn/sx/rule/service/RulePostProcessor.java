package cn.sx.rule.service;

import cn.sx.rule.domain.RuleDTO;
import cn.sx.rule.domain.RuleVO;
import org.springframework.lang.Nullable;

public interface RulePostProcessor {

    @Nullable
    default void process(RuleDTO ruleDTO, RuleVO ruleVO) {
    }
}
