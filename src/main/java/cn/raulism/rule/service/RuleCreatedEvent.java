package cn.raulism.rule.service;

import cn.raulism.rule.domain.RuleDTO;
import cn.raulism.rule.domain.RuleVO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

public class RuleCreatedEvent extends ApplicationEvent {
    @Getter
    @Setter
    private RuleVO ruleVO;

    public RuleCreatedEvent(RuleDTO ruleDTO, RuleVO ruleVO) {
        super(ruleDTO);
        this.ruleVO = ruleVO;
    }
}
