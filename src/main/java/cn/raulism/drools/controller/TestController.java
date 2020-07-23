package cn.raulism.drools.controller;

import cn.raulism.drools.config.KieUtils;
import cn.raulism.drools.config.RuleLoadUtils;
import cn.raulism.drools.model.Address;
import cn.raulism.drools.model.DrlFact;
import cn.raulism.drools.model.fact.AddressCheckResult;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RequestMapping("/test")
@Controller
public class TestController {

    @ResponseBody
    @GetMapping("/address")
    public void test() {
        //kmodule.xml 中定义的 ksession name

        RuleLoadUtils.loadForRule();
//        for (int i = 0; i < 1000; i++) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                KieSession kieSession = KieUtils.getKieContainer().newKieSession();

                Address address = new Address();
                DrlFact drlFact = new DrlFact();
                drlFact.setName("name");
                drlFact.setResultDesc("hakh我爱好");
                List<DrlFact> drlFacts = new ArrayList<>();
                drlFacts.add(drlFact);
                Map<String, DrlFact> map = Maps.newHashMap();
                map.put("1", drlFact);
                address.setFactMap(map);
                address.setFacts(drlFacts);
                List<AddressCheckResult> list = new ArrayList<>();
                kieSession.setGlobal("list", list);
                kieSession.insert(address);
                int ruleFiredCount = kieSession.fireAllRules();
                kieSession.destroy();
                System.out.println("触发了" + ruleFiredCount + "条规则");
                System.out.println(JSON.toJSONString(list));
            }
        }).start();
//        }
    }
}
