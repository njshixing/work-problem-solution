package cn.raulism.rule.domain;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.exception.FunctionNotFoundException;
import com.googlecode.aviator.runtime.RuntimeUtils;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.*;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class TestMain {
    static Random r = new Random();

    public void main() {

        System.out.println("================1=================");
        List<VisitInfo> visitInfoList = new ArrayList<>();
        for (int m = 0; m < 1; m++) {
            VisitInfo visitInfo = new VisitInfo();
            List<BillInfo> billInfos = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                BillInfo billInfo = new BillInfo();
                billInfo.setAmount(r.nextDouble() * 100);
                billInfo.setBillDate(new Date());
                billInfo.setId(i);
                billInfo.setName("name" + i);
                List<BillDetailInfo> detailInfoList = new ArrayList<>();
                for (int j = 0; j < 50; j++) {
                    BillDetailInfo billDetailInfo = new BillDetailInfo();
                    billDetailInfo.setId(j + 1);
                    billDetailInfo.setBuyDate(DateUtils.addDays(new Date(), -j));
                    billDetailInfo.setType(String.valueOf(j % 3));
                    billDetailInfo.setProject("p00" + j);
                    billDetailInfo.setSumAmount(BigDecimal.valueOf(r.nextDouble() * 100));
                    detailInfoList.add(billDetailInfo);
                }
                billInfo.setDetailList(detailInfoList);
                billInfos.add(billInfo);
            }
            visitInfo.setBillList(billInfos);
            visitInfo.setIdHomePage("123");
            visitInfo.setVisitNo("233");
            visitInfo.setInDate(new Date());
            visitInfo.setOutDate(DateUtils.addDays(new Date(), 10));
            visitInfoList.add(visitInfo);
        }
        System.out.println("=================2================");
        List<String> expList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String exp1 = "let visitInfo.billList = map(visitInfo.billList,statDetail);";
            expList.add(exp1);
        }
        System.out.println("=================4================");
        // 注册函数
        AviatorEvaluator.addFunction(new billStatFunction());
        AviatorEvaluator.addFunction(new BillDetailStatFunction());
        AviatorEvaluator.addFunction(new statDetailFunction());
        AviatorEvaluator.addFunction(new VisitFunction());
        System.out.println("==================5===============");
        for (String exp1 : expList) {
            AviatorEvaluator.compile(exp1, true);
        }
        System.out.println("===================6==============");
        long t1 = System.currentTimeMillis();
        for (VisitInfo visitInfo : visitInfoList) {
            for (String exp1 : expList) {
                Map<String, Object> env = new HashMap<>();
                env.put("visitInfo", visitInfo);
                AviatorEvaluator.execute(exp1, env);
            }
        }
        System.out.println("==================7===============" + (System.currentTimeMillis() - t1));
    }

    static class VisitFunction extends AbstractFunction {
        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
            Object first = arg1.getValue(env);
            return AviatorRuntimeJavaType.valueOf(null);
        }
        public String getName() {
            return "result";
        }
    }

    static class billStatFunction extends AbstractFunction {
        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
            Object first = arg1.getValue(env);
            AviatorFunction fun = FunctionUtils.getFunction(arg2, env, 1);
            if (fun == null) {
                throw new FunctionNotFoundException(
                        "There is no function named " + ((AviatorJavaType) arg2).getName());
            }
            Sequence seq = RuntimeUtils.seq(first, env);
            Collector collector = seq.newCollector(0);
            for (Object obj : seq) {
                BillInfo billInfo = (BillInfo) obj;
                Object o = fun.call(env, AviatorRuntimeJavaType.valueOf(obj), AviatorRuntimeJavaType.valueOf(billInfo.getDetailList()));
                collector.add(o);
                return AviatorRuntimeJavaType.valueOf(o);
            }
            return AviatorRuntimeJavaType.valueOf(collector.getRawContainer());
        }

        public String getName() {
            return "billStat";
        }
    }

    static class BillDetailStatFunction extends AbstractFunction {
        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
            Object first = arg1.getValue(env);
            System.out.println(arg2.getAviatorType());
            Object second = arg2.getValue(env);
            Sequence seq = RuntimeUtils.seq(second, env);
            BillInfo billInfo = (BillInfo) first;
            Double s = 0.0d;
            Double m = 0.0d;
            for (Object obj : seq) {
                if (obj instanceof BillDetailInfo) {
                    BillDetailInfo detailInfo = (BillDetailInfo) obj;
                    s = s + detailInfo.getSumAmount().doubleValue();
                    if (detailInfo.getType().equals("2")) {
                        m += detailInfo.getSumAmount().doubleValue();
                    }
                }
            }
            billInfo.setAmount(m / s);
            return AviatorRuntimeJavaType.valueOf(billInfo);
        }

        public String getName() {
            return "billDetailStat";
        }
    }

    static class statDetailFunction extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
            Object first = arg1.getValue(env);
            if (first instanceof BillInfo) {
                BillInfo billInfo = (BillInfo) first;
                double s = 0.0d;
                double m = 0.0d;
                for (BillDetailInfo detailInfo : billInfo.getDetailList()) {
                    s = s + detailInfo.getSumAmount().doubleValue();
                    if (detailInfo.getType().equals("2")) {
                        m += detailInfo.getSumAmount().doubleValue();
                    }
                }
                billInfo.setAmount(m / s);
                billInfo.setPrice(BigDecimal.valueOf(s));
                return AviatorRuntimeJavaType.valueOf(billInfo);
            }
            return AviatorNil.NIL;
        }

        public String getName() {
            return "statDetail";
        }
    }


}
