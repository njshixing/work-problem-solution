package cn.sx.rule.service;

import cn.sx.rule.domain.RuleDTO;
import cn.sx.rule.domain.RuleVO;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class RuleEventListener {
    /**
     * 1. 发送邮件、短信
     */
    @EventListener
    public void processAccountCreatedEvent1(RuleCreatedEvent event) {
        RuleVO ruleVO = event.getRuleVO();
        RuleDTO ruleDTO = (RuleDTO) event.getSource();
        System.out.println(ruleDTO.getId());
        System.out.println(ruleVO.getRuleCat());
    }

    /**
     * 2. 添加积分等，@Order(100) 用来设定执行顺序
     */
    @EventListener
    @Order(100)
    public void processAccountCreatedEvent2(RuleUpdatedEvent event) {
        // TODO
        System.out.println("====");
    }
}
