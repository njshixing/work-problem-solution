package cn.sx.rule.service;

import cn.sx.common.SpringContextUtil;
import cn.sx.rule.domain.RuleDTO;
import cn.sx.rule.domain.RuleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DefaultRuleProcessor implements RuleProcessor {
    @Autowired
    private ApplicationEventPublisher publisher;

    public RuleDTO process(RuleVO ruleVO) throws Exception {
        DefaultRuleProcessor processor = getProcessor(ruleVO);
        String check = processor.beforeProcess(ruleVO);
        if (!"".equals(check)) {
            throw new Exception(check);
        }
        RuleDTO ruleDTO = processor.ruleProcessing(ruleVO);
        processor.afterProcess(ruleDTO, ruleVO);
        doRulePostProcessors(ruleDTO, ruleVO);
        doPublishEvent(ruleDTO, ruleVO);
        return ruleDTO;
    }

    @Override
    public String beforeProcess(RuleVO ruleVO) {
        // 通用校验
        log.info("通用校验");
        return "";
    }

    private DefaultRuleProcessor getProcessor(RuleVO ruleVO) {
        String processorName = ruleVO.getRuleCat();
        if (SpringContextUtil.getApplicationContext().containsBean(processorName)) {
            return SpringContextUtil.getBean(processorName);
        } else {
            return this;
        }
    }

    private void doRulePostProcessors(RuleDTO ruleDTO, RuleVO ruleVO) {
        String[] postProcessorNames = SpringContextUtil.getApplicationContext().getBeanNamesForType(RulePostProcessor.class);
        List<RulePostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
        for (String ppName : postProcessorNames) {
            RulePostProcessor pp = SpringContextUtil.getBean(ppName);
            priorityOrderedPostProcessors.add(pp);
        }
        priorityOrderedPostProcessors.sort(OrderComparator.INSTANCE);
        for (RulePostProcessor pp : priorityOrderedPostProcessors) {
            pp.process(ruleDTO, ruleVO);
        }
    }

    private void doPublishEvent(RuleDTO ruleDTO, RuleVO ruleVO) {
        publisher.publishEvent(new RuleCreatedEvent(ruleDTO, ruleVO));
        publisher.publishEvent(new RuleUpdatedEvent(ruleDTO, ruleVO));
    }
}
