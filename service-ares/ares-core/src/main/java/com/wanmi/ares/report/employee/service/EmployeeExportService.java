package com.wanmi.ares.report.employee.service;

import com.wanmi.ares.report.base.model.ExportQuery;
import com.wanmi.ares.report.employee.dao.EmployeeClientReportMapper;
import com.wanmi.ares.report.employee.dao.EmployeePerformanceReportMapper;
import com.wanmi.ares.source.service.StoreService;
import com.wanmi.ares.utils.excel.Column;
import com.wanmi.ares.utils.excel.ExcelHelper;
import com.wanmi.ares.utils.excel.impl.SpelColumnRender;
import com.wanmi.ares.utils.osd.OsdService;
import com.wanmi.ares.view.employee.EmployeeClientView;
import com.wanmi.ares.view.employee.EmployeePerformanceView;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>业务员报表下载实现</p>
 * Created by of628-wenzhi on 2017-11-03-上午11:44.
 */
@Service
@Slf4j
public class EmployeeExportService {

    @Resource
    private EmployeeQueryServiceImpl queryService;

    @Resource
    private OsdService osdService;

    @Resource
    private EmployeeClientReportMapper clientMapper;

    @Resource
    private EmployeePerformanceReportMapper performanceMapper;

    @Resource
    private StoreService storeService;

    private static final int size = 5000;

    /**
     * 业务员获客报表下载
     *
     * @param query 下载参数
     * @return 报表在OSS的path集合
     * @throws Exception
     */
    public List<String> exportClientReport(ExportQuery query) throws Exception {
        int i = 0;
        List<String> locations = new ArrayList<>();
        long count = clientMapper.countOfExport(query);
        List<EmployeeClientView> records;
        if (count != 0) {
            query.setSize(size);
            while (i < count) {
                query.setBeginIndex(i);
                records = queryService.exportViewByClient(query);
                //生成文件并上传
                uploadClientFile(records, locations, query);
                i += records.size();
            }
        } else {
            records = Collections.emptyList();
            uploadClientFile(records, locations, query);
        }

        return locations;
    }

    /**
     * 业务员业绩报表下载
     *
     * @param query 下载参数
     * @return 报表在OSS的path集合
     * @throws Exception
     */
    public List<String> exportPerformanceReport(ExportQuery query) throws Exception {
        int i = 0;
        List<String> locations = new ArrayList<>();
//        long count = performanceMapper.countOfExport(query);
        List<EmployeePerformanceView> records = queryService.exportViewByPerformance(query);
        long count = records.size();
        if (count != 0) {
//            query.setSize(size);
            while (i < count) {
//                query.setBeginIndex(i);
//                records = queryService.exportViewByPerformance(query);
                List<EmployeePerformanceView> list = records.stream().skip(i).limit(size).collect(Collectors.toList());
                //生成文件并上传
                uploadPerformanceFile(list, locations, query);
                i += list.size();
            }
        } else {
            records = Collections.emptyList();
            uploadPerformanceFile(records, locations, query);
        }

        return locations;
    }

    private void uploadPerformanceFile(List<EmployeePerformanceView> records, List<String> locations, ExportQuery query) throws Exception {
        //如果没有报表数据，则生成只有表头的excel文件
        ExcelHelper<EmployeePerformanceView> excelHelper = new ExcelHelper<>();
        excelHelper.addSheet(
                "业务员业绩统计",
                new Column[]{
                        new Column("业务员", new SpelColumnRender<EmployeePerformanceView>("employeeName")),
                        new Column("下单笔数", new SpelColumnRender<EmployeePerformanceView>("orderCount")),
                        new Column("下单人数", new SpelColumnRender<EmployeePerformanceView>("customerCount")),
                        new Column("下单金额", new SpelColumnRender<EmployeePerformanceView>("amount")),
                        new Column("付款订单数", new SpelColumnRender<EmployeePerformanceView>("payCount")),
                        new Column("付款人数", new SpelColumnRender<EmployeePerformanceView>("payCustomerCount")),
                        new Column("付款金额", new SpelColumnRender<EmployeePerformanceView>("payAmount")),
                        new Column("客单价", new SpelColumnRender<EmployeePerformanceView>("customerUnitPrice")),
                        new Column("笔单价", new SpelColumnRender<EmployeePerformanceView>("orderUnitPrice")),
                        new Column("退单笔数", new SpelColumnRender<EmployeePerformanceView>("returnCount")),
                        new Column("退单人数", new SpelColumnRender<EmployeePerformanceView>("returnCustomerCount")),
                        new Column("退单金额", new SpelColumnRender<EmployeePerformanceView>("returnAmount")),
                },
                records
        );
        String fileName = String.format("employee/performance/%s/%s业务员业绩统计报表_%s-%s%s.xls", StringUtils.left(query.getDateTo(),
                query.getDateTo().lastIndexOf('-')), storeService.getStoreName(query), query.getDateFrom(), query.getDateTo(),
                locations.isEmpty() ? "" : "(" + locations.size() + ")");
        fileName = osdService.getFileRootPath().concat(fileName);
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            excelHelper.write(os);
            osdService.uploadExcel(os, fileName);
            locations.add(fileName);
        }
    }

    private void uploadClientFile(List<EmployeeClientView> records, List<String> locations, ExportQuery query)
            throws Exception {
        //如果没有报表数据，则生成只有表头的excel文件
        ExcelHelper<EmployeeClientView> excelHelper = new ExcelHelper<>();
        excelHelper.addSheet(
                "业务员获客统计",
                new Column[]{
                        new Column("业务员", new SpelColumnRender<EmployeeClientView>("employeeName")),
                        new Column("新增客户数", new SpelColumnRender<EmployeeClientView>("newlyNum"))
                },
                records
        );
        String fileName = String.format("employee/client/%s/%s业务员获客统计报表_%s-%s%s.xls",
                StringUtils.left(query.getDateTo(), query.getDateTo().lastIndexOf('-')),
                storeService.getStoreName(query),
                query.getDateFrom(),
                query.getDateTo(),
                locations.isEmpty() ? "" : "(" + locations.size() + ")"
        );
        fileName = osdService.getFileRootPath().concat(fileName);
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            excelHelper.write(os);
            osdService.uploadExcel(os, fileName);
            locations.add(fileName);
        }

    }

}
