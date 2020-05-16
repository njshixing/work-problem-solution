package cn.raulism.rule.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class VisitInfo {
    private String idHomePage;
    private String visitNo;
    private Date inDate;
    private Date outDate;
    private BigDecimal totalAmount;
    private List<BillInfo> billList;
    private Integer days;

}
