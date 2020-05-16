package cn.raulism.rule.service;

import cn.raulism.rule.domain.RuleDTO;
import cn.raulism.rule.domain.RuleVO;
import org.springframework.stereotype.Service;

@Service
public class BysjRuleProcessor extends DefaultRuleProcessor {
    @Override
    public RuleDTO ruleProcessing(RuleVO ruleVO) {
        System.out.println("hahahhaha");
        return new RuleDTO();
    }

    @Override
    public void afterProcess(RuleDTO ruleDTO, RuleVO ruleVO) {
        System.out.println("aaaaaaaaaaaaaaa");
    }
}
