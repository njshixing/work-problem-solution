package cn.sx.rule.service;

import cn.sx.rule.domain.RuleDTO;
import cn.sx.rule.domain.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@Service
public class UserService {

    @Autowired
    private HttpServletRequest request;

    @GetMapping(value = "/api/get111")
    public String getUserName(@RequestParam(name = "id") String id, @RequestParam(name = "name", required = false) String name) {
        return "name" + id;
    }

    public String setUserName(@RequestBody RuleDTO ruleDTO) {
        return "sssss";
    }

    public String export(HttpServletResponse response) throws Exception {
//
//        // 导出EXCEL文件名称
//        String filaName = "用户EXCEL";
//
//        Map<String, String[]> sheetTitleMap = new HashMap<>();
//        // 标题
//        String[] titles = {"账号", "密码", "状态", "昵称", "职位", "手机号", "邮箱", "创建人ID", "创建时间", "修改人ID", "修改时间"};
//        sheetTitleMap.put("手术", titles);
//        sheetTitleMap.put("手术2", titles);
//        // 开始导入
//        PoiUtil.exportExcelToWebsite(response, filaName, sheetTitleMap, new WriteExcelDataDelegated() {
//            @Override
//            public void writeExcelData(SXSSFSheet eachSheet, Integer startRowCount, Integer endRowCount, Integer currentPage, Integer pageSize) throws Exception {
//
//                List<UserVO> userVOList = selectUserVOList();
//                System.out.println(userVOList.size());
//                if (!userVOList.isEmpty()) {
//                    // --------------   这一块变量照着抄就行  强迫症 后期也封装起来     ----------------------
//                    for (int i = startRowCount; i <= endRowCount; i++) {
//                        SXSSFRow eachDataRow = eachSheet.createRow(i);
//                        if ((i - startRowCount) < userVOList.size()) {
//                            UserVO eachUserVO = userVOList.get(i - startRowCount);
//                            // ---------   这一块变量照着抄就行  强迫症 后期也封装起来     -----------------------
//                            eachDataRow.createCell(0).setCellValue(eachUserVO.getName() == null ? "" : eachUserVO.getName());
//                            eachDataRow.createCell(1).setCellValue(eachUserVO.getPassword() == null ? "" : eachUserVO.getPassword());
//                            eachDataRow.createCell(2).setCellValue(eachUserVO.getSex() == null ? "" : (eachUserVO.getSex().equals("1") ? "男" : "女"));
//                            eachDataRow.createCell(3).setCellValue(eachUserVO.getMobile() == null ? "" : eachUserVO.getMobile());
//                            eachDataRow.createCell(4).setCellValue(eachUserVO.getEmail() == null ? "" : eachUserVO.getEmail());
//                        }
//                    }
//                }
//
//            }
//        });

//        // 开始导入
//        PoiUtil.exportExcelToWebsite(response, totalRowCount, filaName, titles, new WriteExcelDataDelegated() {
//            @Override
//            public void writeExcelData(SXSSFSheet eachSheet, Integer startRowCount, Integer endRowCount, Integer currentPage, Integer pageSize) throws Exception {
//
//                PageHelper.startPage(currentPage, pageSize);
//                List<UserVO> userVOList = userMapper.selectUserVOList(userVO);
//                if (!userVOList.isEmpty()) {
//                    // --------------   这一块变量照着抄就行  强迫症 后期也封装起来     ----------------------
//                    for (int i = startRowCount; i <= endRowCount; i++) {
//                        SXSSFRow eachDataRow = eachSheet.createRow(i);
//                        if ((i - startRowCount) < userVOList.size()) {
//                            UserVO eachUserVO = userVOList.get(i - startRowCount);
//                            // ---------   这一块变量照着抄就行  强迫症 后期也封装起来     -----------------------
//                            eachDataRow.createCell(0).setCellValue(eachUserVO.getName() == null ? "" : eachUserVO.getName());
//                            eachDataRow.createCell(1).setCellValue(eachUserVO.getPassword() == null ? "" : eachUserVO.getPassword());
//                            eachDataRow.createCell(2).setCellValue(eachUserVO.getSex() == null ? "" : (eachUserVO.getSex().equals( "1") ? "男" : "女"));
//                            eachDataRow.createCell(3).setCellValue(eachUserVO.getMobile() == null ? "" : eachUserVO.getMobile());
//                            eachDataRow.createCell(4).setCellValue(eachUserVO.getEmail() == null ? "" : eachUserVO.getEmail());
//                        }
//                    }
//                }
//
//            }
//        });
        return "导出用户EXCEL成功";
    }

    private List<UserVO> selectUserVOList() {
        List<UserVO> list = new ArrayList<>();
        for (int i = 0; i < 200000; i++) {
            UserVO userVO = new UserVO();
            userVO.setName("aa" + i);
            userVO.setEmail("bb" + i);
            userVO.setMobile("cc" + i);
            userVO.setPassword("dd" + i);
            userVO.setSex(String.valueOf(i % 2));
            list.add(userVO);
        }
        return list;
    }
}
