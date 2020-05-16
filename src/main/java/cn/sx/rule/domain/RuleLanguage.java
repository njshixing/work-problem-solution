package cn.sx.rule.domain;

import lombok.Data;

import java.util.List;

@Data
public class RuleLanguage {
    private String ruleCode;
    private String exp;
    private String resultPatten;
    private List<String> results;
    private List<BillInfo> bills;
    private List<BillDetailInfo> details;
    private VisitInfo visitInfo;

    private boolean isViolate;
}
