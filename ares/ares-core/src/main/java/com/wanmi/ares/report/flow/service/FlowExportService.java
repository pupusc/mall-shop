package com.wanmi.ares.report.flow.service;

import com.wanmi.ares.enums.SortOrder;
import com.wanmi.ares.report.base.model.ExportQuery;
import com.wanmi.ares.report.customer.dao.ReplayStoreMapper;
import com.wanmi.ares.report.flow.model.reponse.FlowReponse;
import com.wanmi.ares.report.flow.model.request.FlowReportRequest;
import com.wanmi.ares.utils.Constants;
import com.wanmi.ares.utils.DateUtil;
import com.wanmi.ares.utils.excel.Column;
import com.wanmi.ares.utils.excel.ExcelHelper;
import com.wanmi.ares.utils.excel.impl.SpelColumnRender;
import com.wanmi.ares.utils.osd.OsdService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

/**
 * 流量报表下载实现
 * Created by sunkun on 2017/11/8.
 */
@Service
public class FlowExportService {

    @Resource
    private FlowReportService flowReportService;

    @Resource
    private OsdService osdService;

    @Resource
    private ReplayStoreMapper replayStoreMapper;

    /**
     *  流量报表生成并上传
     * @param query
     * @return
     * @throws Exception
     */
    public String exportFlowReport(ExportQuery query) throws Exception {
        FlowReportRequest flowReportRequest = new FlowReportRequest();
        flowReportRequest.setBeginDate(DateUtil.parse2Date(query.getDateFrom(), DateUtil.FMT_DATE_1));
        flowReportRequest.setEndDate(DateUtil.parse2Date(query.getDateTo(), DateUtil.FMT_DATE_1));
        flowReportRequest.setCompanyId(query.getCompanyId());
        flowReportRequest.setPageSize(365);
        flowReportRequest.setSortOrder(SortOrder.DESC);
        Page<FlowReponse> page = flowReportService.getPage(flowReportRequest);
        List<FlowReponse> flowReponseList = page.getContent();
        LocalDate endDate = DateUtil.parse2Date(query.getDateTo(), DateUtil.FMT_DATE_1);
        ExcelHelper<FlowReponse> excelHelper = new ExcelHelper<>();
        String fileName = String.format("flow/%s/%s/流量统计报表_%s-%s.xls", DateUtil.format(endDate, DateUtil.FMT_MONTH_2),query.getCompanyId(), query.getDateFrom(), query.getDateTo());
        if(!"0".equals(query.getCompanyId())){
            String storeName = replayStoreMapper.findCompanyName(query.getCompanyId());
            fileName = String.format("flow/%s/%s/%s_流量统计报表_%s-%s.xls", DateUtil.format(endDate, DateUtil.FMT_MONTH_2),query.getCompanyId(),storeName, query.getDateFrom(), query.getDateTo());
        }
        fileName = osdService.getFileRootPath().concat(fileName);
        if (flowReponseList == null || flowReponseList.isEmpty()) {
//            String emptyFile = "flow/流量统计报表_"+query.getCompanyId()+".xls";
            if (osdService.existsFiles(fileName)) {
                return fileName;
            }
        }
        excelHelper.addSheet(
                "流量报表统计",
                new Column[]{
                        new Column("日期", (cell, object) -> {
                            FlowReponse flowReponse = (FlowReponse) object;
                            cell.setCellValue(DateUtil.format(flowReponse.getDate(), DateUtil.FMT_DATE_1));
                        }),
                        new Column("访客数UV", new SpelColumnRender<FlowReponse>("totalUv")),
                        new Column("浏览量PV", new SpelColumnRender<FlowReponse>("totalPv")),
                        new Column("商品访客数", new SpelColumnRender<FlowReponse>("skuTotalUv")),
                        new Column("商品浏览量", new SpelColumnRender<FlowReponse>("skuTotalPv")),
                },
                flowReponseList
        );

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            excelHelper.write(os);
            osdService.uploadExcel(os, fileName);
        }
        return fileName;
    }

    /**
     * 店铺报表生成并上传
     * @param query
     * @return
     * @throws Exception
     */
    public String exportStoreFlowReport(ExportQuery query) throws Exception{
        FlowReportRequest flowReportRequest = new FlowReportRequest();
        flowReportRequest.setBeginDate(DateUtil.parse2Date(query.getDateFrom(), DateUtil.FMT_DATE_1));
        flowReportRequest.setEndDate(DateUtil.parse2Date(query.getDateTo(), DateUtil.FMT_DATE_1));
        flowReportRequest.setPageSize(5000);
        flowReportRequest.setSortName("totalPv");
        flowReportRequest.setSortOrder(SortOrder.DESC);
        Page<FlowReponse> page = flowReportService.getStoreList(flowReportRequest);
        List<FlowReponse> flowReponseList = page.getContent();
        LocalDate endDate = DateUtil.parse2Date(query.getDateTo(), DateUtil.FMT_DATE_1);
        ExcelHelper<FlowReponse> excelHelper = new ExcelHelper<>();
        String fileName = String.format("flow/%s/店铺流量统计报表_%s-%s.xls", DateUtil.format(endDate, DateUtil.FMT_MONTH_2), query.getDateFrom(), query.getDateTo());
        fileName = osdService.getFileRootPath().concat(fileName);
        if (flowReponseList == null || flowReponseList.isEmpty()) {
//            String emptyFile = "flow/店铺流量.xls";
            if (osdService.existsFiles(fileName)) {
                return fileName;
            }
        }
        excelHelper.addSheet(
                "店铺流量报表统计",
                new Column[]{
                        new Column("店铺名称",new SpelColumnRender<FlowReponse>("title")),
                        new Column("访客数UV", new SpelColumnRender<FlowReponse>("totalUv")),
                        new Column("浏览量PV", new SpelColumnRender<FlowReponse>("totalPv")),
                        new Column("商品访客数", new SpelColumnRender<FlowReponse>("skuTotalUv")),
                        new Column("商品浏览量", new SpelColumnRender<FlowReponse>("skuTotalPv")),
                },
                flowReponseList
        );

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            excelHelper.write(os);
            osdService.uploadExcel(os, fileName);
        }
        return fileName;
    }
}
