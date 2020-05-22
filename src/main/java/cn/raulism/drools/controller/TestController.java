package cn.raulism.drools.controller;

import cn.raulism.drools.model.Address;
import cn.raulism.drools.model.DrlFact;
import cn.raulism.drools.model.fact.AddressCheckResult;
import com.alibaba.fastjson.JSON;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@RequestMapping("/test")
@Controller
public class TestController {

    @Resource
    private KieContainer kieContainer;

    @ResponseBody
    @GetMapping("/address")
    public void test() {
        //kmodule.xml 中定义的 ksession name
        KieSession kieSession = kieContainer.newKieSession();

        Address address = new Address();
        DrlFact drlFact = new DrlFact();
        drlFact.setName("hakh我爱好");
        List<DrlFact> drlFacts = new ArrayList<>();
        drlFacts.add(drlFact);
        address.setFacts(drlFacts);
        List<AddressCheckResult> list = new ArrayList<>();
        kieSession.setGlobal("list", list);
        kieSession.insert(address);
        int ruleFiredCount = kieSession.fireAllRules();
        kieSession.destroy();
        System.out.println("触发了" + ruleFiredCount + "条规则");

        System.out.println(JSON.toJSONString(address));
    }
}
