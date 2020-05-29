package cn.raulism.drools.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieContainer;

public class RuleLoadUtils {
    public static void loadForRule() {
        KieUtils.initAndNotClear();
        String rule = "package rules;\n" +
                "import cn.raulism.drools.model.Address;\n" +
                "import cn.raulism.drools.model.fact.AddressCheckResult;\n" +
                "import cn.raulism.drools.model.DrlFact;\n" +
                "\n" +
                "global java.util.List list\n" +
                "\n" +
                "rule \"Postcode should be filled with exactly 5 numbers\"\n" +
                "    when\n" +
                "        address : Address(null != facts,facts.size > 0)\n" +
                "    then\n" +
                "        for(DrlFact fact : address.getFacts()){\n" +
                "            AddressCheckResult result = new AddressCheckResult();\n" +
                "            fact.setResultDesc(fact.getName()+\"====\");\n" +
                "            list.add(fact);\n" +
                "        }\n" +
                "\t\tSystem.out.println(\"规则中打印日志：校验通过!\");\n" +
                "end";
        KieFileSystem kfs = KieUtils.getKieFileSystem();
        KieServices ks = KieUtils.getKieServices();
        KieRepository kr = KieUtils.getKieRepository();

        kfs.write("src/main/resources/rules/R001.drl", rule);

        // 将KieFileSystem加入到KieBuilder
        KieBuilder kb = ks.newKieBuilder(kfs);
        // 编译此时的builder中所有的规则
        kb.buildAll();
        if (kb.getResults().hasMessages(Message.Level.ERROR)) {
            throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
        }
        KieContainer kieContainer = ks.newKieContainer(kr.getDefaultReleaseId());
        KieUtils.setKieContainer(kieContainer);
    }
}