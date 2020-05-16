package cn.sx.rule.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ExcelDelegated<T> {

    default List<T> writeData(int j, int pageSize, Map<String, Object> queryExcelDataMap) {
        return new ArrayList<>();
    }

    default List<T> writeMultiSheetData(int j, int pageSize, Class<?> clazz, Map<Class<?>, Map<String, Object>> beanTypeAndQueryMap, Set<Class<?>> keySet) {
        return new ArrayList<>();
    }

    default void readData(List<T> beanList) {
    }
}
