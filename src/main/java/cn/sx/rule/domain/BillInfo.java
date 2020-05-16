package cn.sx.rule.domain;

import cn.sx.excel.ImsExcel;
import cn.sx.excel.ImsExcelCell;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ImsExcel(sheetName = "haha")
public class BillInfo {
    private Integer id;

    @ImsExcelCell(name = "名称")
    private String name;
    private Date billDate;
    @ImsExcelCell(name = "价格")
    private BigDecimal price;
    private Double amount;
    private List<BillDetailInfo> detailList;

}
