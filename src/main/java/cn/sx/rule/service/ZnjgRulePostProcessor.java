package cn.sx.rule.service;

import cn.sx.rule.domain.RuleDTO;
import cn.sx.rule.domain.RuleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ZnjgRulePostProcessor implements RulePostProcessor, PriorityOrdered {

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void process(RuleDTO ruleDTO, RuleVO ruleVO) {
        log.info("=================== ZnjgRulePostProcessor ===================");
    }
}
