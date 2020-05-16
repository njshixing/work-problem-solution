package cn.sx.rule.service;

import cn.sx.rule.domain.RuleDTO;
import cn.sx.rule.domain.RuleVO;
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
