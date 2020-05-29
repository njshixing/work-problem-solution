package cn.raulism.drools.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;

import java.util.Objects;

public class KieUtils {
    private static KieContainer kieContainer;

    private static KieServices kieServices;

    private static KieRepository kieRepository;

    private static KieFileSystem kieFileSystem;

    public static void initAndNotClear() {
        if (Objects.isNull(kieServices))
            kieServices = KieServices.Factory.get();

        if (Objects.isNull(kieRepository))
            kieRepository = kieServices.getRepository();

        if (Objects.isNull(kieFileSystem))
            kieFileSystem = kieServices.newKieFileSystem();
    }

    public static KieContainer getKieContainer() {
        return kieContainer;
    }

    public static void setKieContainer(KieContainer kieContainer) {
        KieUtils.kieContainer = kieContainer;
    }

    public static KieServices getKieServices() {
        return kieServices;
    }

    public static KieRepository getKieRepository() {
        return kieRepository;
    }

    public static KieFileSystem getKieFileSystem() {
        return kieFileSystem;
    }
}
