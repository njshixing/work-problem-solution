package cn.sx.excel;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.PageUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import cn.sx.rule.service.ExcelDelegated;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class PoiUtil {
    private static final int PAGE_SIZE = 100000;

    /**
     * 读取单个sheet内容
     *
     * @param in             读取对象
     * @param startRowIndex  开始读取数据的行数
     * @param batchRowNumber 每次读取的行数
     * @param ignoreEmptyRow 是否忽略空行
     * @param beanType       读取后转换的类型
     * @param excelDelegated 批量读取数据委托类
     * @param <T>            T
     * @throws Exception exception
     */
    public static <T> void readExcel(InputStream in, int startRowIndex, int batchRowNumber, boolean ignoreEmptyRow,
                                     Class<T> beanType, ExcelDelegated<T> excelDelegated) throws Exception {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(beanType, ImsExcelCell.class);
        List<String> cellName = new ArrayList<>();
        for (Field field : fields) {
            cellName.add(field.getName());
        }
        List<T> beanList = new ArrayList<>(batchRowNumber);
        ExcelUtil.readBySax(in, -1, new RowHandler() {
            @Override
            public void handle(int sheetIndex, int rowIndex, List<Object> rowList) {
                if (rowIndex >= startRowIndex) {
                    if (CollUtil.isNotEmpty(rowList) || !ignoreEmptyRow) {
                        beanList.add(BeanUtil.mapToBeanIgnoreCase(IterUtil.toMap(cellName, rowList, true), beanType, false));
                    }
                    if (beanList.size() == batchRowNumber) {
                        excelDelegated.readData(beanList);
                        beanList.clear();
                    }
                }
            }
        });
        excelDelegated.readData(beanList);
    }

    /**
     * 读取多个sheet内容
     *
     * @param in               读取对象
     * @param sheetBeanTypeMap 读取的sheet和对应的sheet需要转换的对象
     * @param startRowIndex    开始读取数据的行数
     * @param batchRowNumber   每次读取的行数
     * @param ignoreEmptyRow   是否忽略空行
     * @param excelDelegated   批量读取数据委托类
     * @param <T>              T
     * @throws Exception exception
     */
    public static <T> void readExcelMultiSheet(InputStream in, Map<Integer, Class<?>> sheetBeanTypeMap, int startRowIndex, int batchRowNumber, boolean ignoreEmptyRow,
                                               ExcelDelegated<Object> excelDelegated) throws Exception {
        Map<Integer, List<String>> map = new HashMap<>();
        for (Map.Entry<Integer, Class<?>> entry : sheetBeanTypeMap.entrySet()) {
            List<Field> fields = FieldUtils.getFieldsListWithAnnotation(entry.getValue(), ImsExcelCell.class);
            List<String> cellNameList = new ArrayList<>();
            for (Field field : fields) {
                cellNameList.add(field.getName());
            }
            int currentSheetIndex = entry.getKey();
            map.put(currentSheetIndex, cellNameList);
        }
        List<Object> beanList = new ArrayList<>(batchRowNumber);
        ExcelUtil.readBySax(in, -1, new RowHandler() {
            @Override
            public void handle(int sheetIndex, int rowIndex, List<Object> rowList) {
                if (rowIndex >= startRowIndex) {
                    if (CollUtil.isNotEmpty(rowList) || !ignoreEmptyRow) {
                        beanList.add(BeanUtil.mapToBeanIgnoreCase(IterUtil.toMap(map.get(sheetIndex), rowList, true), sheetBeanTypeMap.get(sheetIndex), false));
                    }
                    if (beanList.size() == batchRowNumber) {
                        excelDelegated.readData(beanList);
                        beanList.clear();
                    }
                }
            }
        });
        excelDelegated.readData(beanList);
    }

    /**
     * 写入Excel文件
     *
     * @param destFile          目标文件
     * @param totalCount        写入数据总行数
     * @param beanType          写入数据的类型
     * @param queryExcelDataMap 批量查询数据的查询条件
     * @param excelDelegated    查询数据委托类
     * @param <T>               T
     * @throws Exception exception
     */
    public static <T> void writeExcel(File destFile, int totalCount, Class<T> beanType, Map<String, Object> queryExcelDataMap, ExcelDelegated<T> excelDelegated) throws Exception {
        ExcelWriter writer = getBigExcelWriter(totalCount, beanType, queryExcelDataMap, excelDelegated);
        writer.flush(destFile).close();
    }

    /**
     * 写入Excel到流
     *
     * @param outputStream      目标流
     * @param totalCount        写入数据总行数
     * @param beanType          写入数据的类型
     * @param queryExcelDataMap 批量查询数据的查询条件
     * @param excelDelegated    查询数据委托类
     * @param <T>               T
     * @throws Exception exception
     */
    public static <T> void writeExcel(OutputStream outputStream, int totalCount, Class<T> beanType, Map<String, Object> queryExcelDataMap, ExcelDelegated<T> excelDelegated) throws Exception {
        ExcelWriter writer = getBigExcelWriter(totalCount, beanType, queryExcelDataMap, excelDelegated);
        writer.flush(outputStream, true).close();
    }

    /**
     * 写入多个sheet数据到文件
     *
     * @param destFile                 目标文件
     * @param beanTypeAndSheetCountMap 每个sheet的数据类型和对应的数据行数
     * @param beanTypeAndQueryMap      每个sheet的数据类型对应的查询条件
     * @param excelDelegated           查询数据委托类
     * @param <T>                      T
     * @throws Exception exception
     */
    public static <T> void writeExcelMultiSheet(File destFile, Map<Class<?>, Integer> beanTypeAndSheetCountMap, Map<Class<?>, Map<String, Object>> beanTypeAndQueryMap,
                                                ExcelDelegated<T> excelDelegated) throws Exception {
        ExcelWriter writer = getBigExcelWriterMultiSheet(beanTypeAndSheetCountMap, beanTypeAndQueryMap, excelDelegated);
        writer.flush(destFile).close();
    }

    /**
     * 写入多个sheet数据到文件
     *
     * @param out                      目标流
     * @param beanTypeAndSheetCountMap 每个sheet的数据类型和对应的数据行数
     * @param beanTypeAndQueryMap      每个sheet的数据类型对应的查询条件
     * @param excelDelegated           查询数据委托类
     * @param <T>                      T
     * @throws Exception exception
     */
    public static <T> void writeExcelMultiSheet(OutputStream out, Map<Class<?>, Integer> beanTypeAndSheetCountMap, Map<Class<?>, Map<String, Object>> beanTypeAndQueryMap,
                                                ExcelDelegated<T> excelDelegated) throws Exception {
        ExcelWriter writer = getBigExcelWriterMultiSheet(beanTypeAndSheetCountMap, beanTypeAndQueryMap, excelDelegated);
        writer.flush(out, true).close();
    }

    private static <T> ExcelWriter getBigExcelWriter(int totalCount, Class<T> beanType, Map<String, Object> queryExcelDataMap, ExcelDelegated<T> excelDelegated) throws Exception {
        String sheetName = beanType.getAnnotation(ImsExcel.class).sheetName();
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(beanType, ImsExcelCell.class);
        List<String> headerRowList = new ArrayList<>();
        Map<String, String> headerAliasMap = new HashMap<>();
        for (Field field : fields) {
            headerRowList.add(field.getAnnotation(ImsExcelCell.class).name());
            headerAliasMap.put(field.getName(), field.getAnnotation(ImsExcelCell.class).name());
        }
        int pageNum = PageUtil.totalPage(totalCount, PAGE_SIZE);
        ExcelWriter bigExcelWriter = new BigExcelWriter().writeHeadRow(headerRowList).setHeaderAlias(headerAliasMap);
        if (StringUtils.isNotBlank(sheetName)) {
            bigExcelWriter.setSheet(sheetName);
        }
        if (StringUtils.isNotBlank(sheetName)) {
            bigExcelWriter.setSheet(sheetName);
        }
        for (int j = 1; j <= pageNum; j++) {
            bigExcelWriter.write(excelDelegated.writeData(j, PAGE_SIZE, queryExcelDataMap));
        }
        return bigExcelWriter;
    }

    private static <T> ExcelWriter getBigExcelWriterMultiSheet(
            Map<Class<?>, Integer> beanTypeAndSheetCountMap, Map<Class<?>, Map<String, Object>> beanTypeAndQueryMap,
            ExcelDelegated<T> excelDelegated) throws Exception {
        Assert.notEmpty(beanTypeAndSheetCountMap);
        ExcelWriter bigExcelWriter = new BigExcelWriter();
        for (Map.Entry<Class<?>, Integer> entry : beanTypeAndSheetCountMap.entrySet()) {
            Class<?> beanType = entry.getKey();
            int totalCount = entry.getValue();
            String sheetName = beanType.getAnnotation(ImsExcel.class).sheetName();
            List<Field> fields = FieldUtils.getFieldsListWithAnnotation(beanType, ImsExcelCell.class);
            List<String> headerRowList = new ArrayList<>();
            Map<String, String> headerAliasMap = new HashMap<>();
            for (Field field : fields) {
                headerRowList.add(field.getAnnotation(ImsExcelCell.class).name());
                headerAliasMap.put(field.getName(), field.getAnnotation(ImsExcelCell.class).name());
            }
            int pageNum = PageUtil.totalPage(totalCount, PAGE_SIZE);
            if (StringUtils.isNotBlank(sheetName)) {
                bigExcelWriter.setSheet(sheetName);
            }
            if (StringUtils.isNotBlank(sheetName)) {
                bigExcelWriter.setSheet(sheetName);
            }
            bigExcelWriter.writeHeadRow(headerRowList).setHeaderAlias(headerAliasMap);
            for (int j = 1; j <= pageNum; j++) {
                bigExcelWriter.write(excelDelegated.writeMultiSheetData(j, PAGE_SIZE, entry.getKey(), beanTypeAndQueryMap, beanTypeAndSheetCountMap.keySet()));
            }
        }
        return bigExcelWriter;
    }
}
