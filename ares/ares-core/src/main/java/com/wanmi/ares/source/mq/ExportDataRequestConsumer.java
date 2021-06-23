package com.wanmi.ares.source.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.ares.enums.ExportStatus;
import com.wanmi.ares.enums.ReportType;
import com.wanmi.ares.export.dao.ExportDataMapper;
import com.wanmi.ares.export.model.entity.ExportDataEntity;
import com.wanmi.ares.report.base.model.ExportQuery;
import com.wanmi.ares.report.customer.service.CustomerReportExportService;
import com.wanmi.ares.report.employee.service.EmployeeExportService;
import com.wanmi.ares.report.flow.service.FlowExportService;
import com.wanmi.ares.report.goods.service.GoodsReportExportService;
import com.wanmi.ares.report.trade.service.TradeExportService;
import com.wanmi.ares.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 导出任务要求的消息队列消费者
 * Author: bail
 * Time: 2017/11/2.10:54
 */
@Slf4j
@Component
@EnableBinding(ExportDataRequestSink.class)
public class ExportDataRequestConsumer {

    @Autowired
    private ExportDataMapper exportDataMapper;

    @Autowired
    private FlowExportService flowExportService;

    @Autowired
    private TradeExportService tradeExportService;

    @Autowired
    private GoodsReportExportService goodsReportExportService;

    @Autowired
    private EmployeeExportService employeeExportService;

    @Autowired
    private CustomerReportExportService customerReportExportService;

    /**
     * 导出任务要求的消费者1
     * 目前只设置一个消费者,即单线程同步生成导出文件
     * @param json
     */
    @Transactional
    @StreamListener(ExportDataRequestSink.INPUT)
    //@StreamListener(MQConstant.Q_ARES_EXPORT_DATA_REQUEST)
    public void produceExportData(String json) {
          //1.转换消费信息,准备生产导出文件
        ExportDataEntity entity = JSONObject.parseObject(json, ExportDataEntity.class);

        //2.根据商家id,开始日期,截止日期,报表类别查询导出成功的任务数据
        entity.setExportStatus(ExportStatus.SUCCESS_EXPORT.getValue());
        List<ExportDataEntity> expResultList = exportDataMapper.queryExportDataRequestList(entity);

        //3.若存在导出成功的文件,则直接利用之前文件
        if(expResultList!=null && !expResultList.isEmpty()){
            entity.setFilePath(expResultList.get(0).getFilePath());
            entity.setExportStatus(ExportStatus.SUCCESS_EXPORT.getValue());//导出成功
            entity.setFinishTime(DateUtil.nowTime());
        }else{
            //4.若不存在导出成功的文件,则调用生成excel服务(根据类别,生产不同的excel文件)
            produceDetail(entity);
        }

        //3.根据生产结果,更新导出状态
        exportDataMapper.updateExportDataRequest(entity);
    }

    /**
     * 根据导出报表类别,生产不同的导出文件
     * @param entity
     */
    private void produceDetail(ExportDataEntity entity){
        ExportQuery exportQuery = new ExportQuery().convertFromRequest(entity);
        List<String> fileList;
        String filePath = null;
        try {
            if (ReportType.FLOW.getValue()==entity.getTypeCd()) {
                filePath = flowExportService.exportFlowReport(exportQuery);
            } else if (ReportType.TRADE.getValue()==entity.getTypeCd()) {
                filePath = tradeExportService.exportTradeReport(exportQuery);
            } else if (ReportType.GOODS_TRADE.getValue()==entity.getTypeCd()) {
                fileList = goodsReportExportService.generateSkuExcel(exportQuery);
                filePath = fileList.stream().collect(Collectors.joining(","));
            } else if (ReportType.GOODS_CATE_TRADE.getValue()==entity.getTypeCd()) {
                filePath = goodsReportExportService.generateCateExcel(exportQuery);
            } else if (ReportType.GOODS_BRAND_TRADE.getValue()==entity.getTypeCd()) {
                filePath = goodsReportExportService.generateBrandExcel(exportQuery);
            } else if (ReportType.CUSTOMER_GROW.getValue()==entity.getTypeCd()) {
                fileList = customerReportExportService.exportCustomerGrowthReport(exportQuery);
                filePath = fileList.stream().collect(Collectors.joining(","));
            } else if (ReportType.CUSTOMER_TRADE.getValue()==entity.getTypeCd()) {
                fileList = customerReportExportService.exportCustomerReport(exportQuery);
                filePath = fileList.stream().collect(Collectors.joining(","));
            } else if (ReportType.CUSTOMER_LEVEL_TRADE.getValue()==entity.getTypeCd()) {
                fileList = customerReportExportService.exportCustomerLevelReport(exportQuery);
                filePath = fileList.stream().collect(Collectors.joining(","));
            } else if (ReportType.CUSTOMER_AREA_TRADE.getValue()==entity.getTypeCd()) {
                fileList = customerReportExportService.exportCustomerAreaReport(exportQuery);
                filePath = fileList.stream().collect(Collectors.joining(","));
            } else if (ReportType.SALESMAN_TRADE.getValue()==entity.getTypeCd()) {
                fileList = employeeExportService.exportPerformanceReport(exportQuery);
                filePath = fileList.stream().collect(Collectors.joining(","));
            } else if (ReportType.SALESMAN_CUSTOMER.getValue()==entity.getTypeCd()) {
                fileList = employeeExportService.exportClientReport(exportQuery);
                filePath = fileList.stream().collect(Collectors.joining(","));
            } else if (ReportType.STORE_CATE_TRADE.getValue()==entity.getTypeCd()) {
                filePath = goodsReportExportService.generateStoreCateExcel(exportQuery);
            } else if (ReportType.STORE_FLOW.getValue()==entity.getTypeCd()) {
                filePath = flowExportService.exportStoreFlowReport(exportQuery);
            } else if (ReportType.STORE_TRADE.getValue()==entity.getTypeCd()) {
                filePath = tradeExportService.exportStoreTradeReport(exportQuery);
            }
            entity.setFilePath(filePath);
            entity.setExportStatus(ExportStatus.SUCCESS_EXPORT.getValue());//导出成功
        }catch (Exception e){
            entity.setExportStatus(ExportStatus.ERROR_EXPORT.getValue());//导出失败
            log.error("produceReport execute error, param={}", entity.toString(), e);
        }
        entity.setFinishTime(DateUtil.nowTime());
    }

}
