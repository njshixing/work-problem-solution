package cn.raulism.rule.service;

import cn.raulism.rule.domain.RuleDTO;
import cn.raulism.rule.domain.RuleVO;
import org.springframework.lang.Nullable;

public interface RulePostProcessor {

    @Nullable
    default void process(RuleDTO ruleDTO, RuleVO ruleVO) {
    }
}
