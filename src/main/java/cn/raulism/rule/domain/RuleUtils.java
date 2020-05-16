package cn.raulism.rule.domain;

import java.util.ArrayList;
import java.util.List;

public class RuleUtils {

    public List<BillInfo> detail(List<BillInfo> billInfoList, Double standard, String type, String result) {
        List<BillInfo> resultList = new ArrayList<>();
        for (BillInfo billInfo : billInfoList) {
            Double p = 0.0d;
            Double s = 0.0d;
            for (BillDetailInfo detailInfo : billInfo.getDetailList()) {
                if (detailInfo.getType().equals(type)) {
                    p += detailInfo.getSumAmount().doubleValue();
                }
                s += detailInfo.getSumAmount().doubleValue();
            }
            if (p / s > standard) {
                resultList.add(billInfo);
            }
        }
        return billInfoList;
    }
}
