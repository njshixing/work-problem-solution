package cn.sx.rule.domain;

import cn.sx.excel.ImsExcel;
import cn.sx.excel.ImsExcelCell;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ImsExcel
public class BillDetailInfo {
    private Integer id;

    @ImsExcelCell(name = "项目编码")
    private String project;

    @ImsExcelCell(name = "一起")
    private Date buyDate;

    @ImsExcelCell(name = "金额")
    private BigDecimal sumAmount;

    @ImsExcelCell(name = "类型")
    private String type;

    @Override
    public String toString() {
        return "BillDetailInfo{" +
                "id=" + id +
                ", project='" + project + '\'' +
                ", buyDate=" + buyDate +
                ", sumAmount=" + sumAmount +
                ", type='" + type + '\'' +
                '}';
    }
}
