package com.cy.pj.common.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.data.CellData;
import com.cy.pj.sys.service.excel.ExcelDataparseSevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;


/**
 * 批量导入Excel监听器
 * @author cy
 *    注意：多线程处理时，不要使用单例模式
 */
public class BatchExcelListener<T extends ExcelDataparseSevice> extends AnalysisEventListener<Map<Integer, String>> {

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(BatchExcelListener.class);

    /**
     * 存储excel数据
     */
    private List<Map<String, Object>> dataList = new ArrayList<>();

    /**
     * 表头
     *  key:列索引(第几列)，String value:列名
     */
    private Map<Integer, String> headMap;

    /**
     * 业务逻辑层
     */
    private T service;

    /**
     * 构造函数
     * @param service 业务逻辑层
     */
    public BatchExcelListener(T service) {
        this.service = service;
    }

    /**
     * 无参构造函数，用于测试。
     */
    public BatchExcelListener() {
    }
    /**
     * 解析excel表头。
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        this.headMap = headMap;
    }

    /**
     * 解析每一条数据都会执行一次invoke方法。
     * @param data
     * @param context
     */
    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        logger.info("Processing row: {}", context.readRowHolder().getRowIndex());
        Map<String, Object> rowData = new HashMap<>();
        for (Map.Entry<Integer, String> entry : data.entrySet()) {
            String columnName = headMap.get(entry.getKey());
            rowData.put(columnName, entry.getValue());
        }
        dataList.add(rowData);

        // 当数据量达到一定程度时，可先处理数据，例如保存到数据库或缓存中
        int memoryMaxNum = 10000;
        if (dataList.size() == memoryMaxNum) {
            processDataInBatch(dataList);
            dataList.clear();
        }
    }

    /**
     * 所有数据解析完后动作。
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 所有数据解析完成后的操作
        System.out.println("All data has been processed.");
        if (!CollectionUtils.isEmpty(dataList)){
            processDataInBatch(dataList);
        }
    }

    /**
     * 处理数据
     * @param dataList
     */
    private void processDataInBatch(List<Map<String, Object>> dataList) {
        int batchSize = 1000;
        for (int i = 0; i < dataList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, dataList.size());
            List<Map<String, Object>> batch = dataList.subList(i, end);
            // 处理当前批次的数据
            processBatch(batch);
        }
    }

    private void processBatch(List<Map<String, Object>> batch) {
        // 在这里处理每个批次的数据，例如保存到数据库
//        service.excelDataDoSomething(batch);
        for (Map<String, Object> row : batch) {
            System.out.println(row);
        }
    }

    private Object convertCellValue(CellData cellData) {
        if (cellData == null) {
            return null;
        }
        switch (cellData.getType()) {
            case STRING:
                return cellData.getStringValue();
            case BOOLEAN:
                return cellData.getBooleanValue();
            case NUMBER:
                return cellData.getNumberValue();
            case DATE:
                return cellData.getFormulaData();
            default:
                return cellData.getStringValue();
        }
    }
}




