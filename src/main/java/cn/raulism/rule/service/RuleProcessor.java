package cn.raulism.rule.service;

import cn.raulism.rule.domain.RuleDTO;
import cn.raulism.rule.domain.RuleVO;
import org.springframework.lang.Nullable;

public interface RuleProcessor {

    @Nullable
    default String beforeProcess(RuleVO ruleVO) {
        return null;
    }

    @Nullable
    default RuleDTO ruleProcessing(RuleVO ruleVO) {
        return null;
    }

    @Nullable
    default void afterProcess(RuleDTO ruleDTO, RuleVO ruleVO) {
    }
}
