package cn.raulism.proxy;

import cn.hutool.aop.aspects.Aspect;
import cn.hutool.aop.aspects.TimeIntervalAspect;
import cn.hutool.aop.proxy.SpringCglibProxyFactory;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.Console;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.lang.reflect.Method;

@RestController
@Slf4j
public class ProxyController {
    @Autowired
    private ProxyServiceImpl proxyService;

    @GetMapping("/proxy")
    public void test(@RequestParam("name") @NotBlank(message = "1111111111111") String name) {
        ProxyServiceImpl aaa = SpringCglibProxyFactory.createProxy(proxyService, TimeIntervalAspect.class);
        ProxyServiceImpl bbb = SpringCglibProxyFactory.createProxy(proxyService, new Aspect() {
            private final TimeInterval interval = new TimeInterval();

            @Override
            public boolean before(Object target, Method method, Object[] args) {
                System.out.println("1111111111");
                interval.start();
                return true;
            }

            @Override
            public boolean after(Object target, Method method, Object[] args, Object returnVal) {
                System.out.println("222222222");
                Console.log("Method [{}.{}] execute spend [{}]ms return value [{}]",
                        target.getClass().getName(), //
                        method.getName(), //
                        interval.intervalMs(), //
                        returnVal);
                return true;
            }

            @Override
            public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
                System.out.println("333333333");
                return true;
            }
        });
        bbb.test();
        aaa.test();
    }
}
