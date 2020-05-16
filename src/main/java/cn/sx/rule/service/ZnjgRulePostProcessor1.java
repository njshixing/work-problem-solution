package cn.sx.rule.service;

import cn.sx.rule.domain.RuleDTO;
import cn.sx.rule.domain.RuleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ZnjgRulePostProcessor1 implements RulePostProcessor, PriorityOrdered {

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void process(RuleDTO ruleDTO, RuleVO ruleVO) {
        log.info("=================== ZnjgRulePostProcessor1 ===================");
    }
}
