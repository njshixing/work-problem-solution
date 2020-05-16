package cn.sx.rule.service;

import cn.sx.rule.domain.RuleDTO;
import cn.sx.rule.domain.RuleVO;
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
