package com.wanmi.ares.report.customer.service;

import com.wanmi.ares.base.PageRequest;
import com.wanmi.ares.report.base.model.ExportQuery;
import com.wanmi.ares.report.customer.dao.AreaDistributeReportMapper;
import com.wanmi.ares.report.customer.dao.CustomerGrowthReportMapper;
import com.wanmi.ares.report.customer.dao.CustomerOrderReportMapper;
import com.wanmi.ares.report.customer.dao.ReplayStoreMapper;
import com.wanmi.ares.report.customer.model.request.CustomerOrderDataRequest;
import com.wanmi.ares.report.customer.model.root.CustomerAreaReport;
import com.wanmi.ares.report.customer.model.root.CustomerGrowthReport;
import com.wanmi.ares.report.customer.model.root.CustomerLevelReport;
import com.wanmi.ares.report.customer.model.root.CustomerReport;
import com.wanmi.ares.request.customer.CustomerGrowthReportRequest;
import com.wanmi.ares.source.model.root.Store;
import com.wanmi.ares.source.model.root.region.City;
import com.wanmi.ares.source.model.root.region.Province;
import com.wanmi.ares.source.service.StoreService;
import com.wanmi.ares.utils.Constants;
import com.wanmi.ares.utils.DateUtil;
import com.wanmi.ares.utils.excel.Column;
import com.wanmi.ares.utils.excel.ExcelHelper;
import com.wanmi.ares.utils.excel.impl.SpelColumnRender;
import com.wanmi.ares.utils.osd.OsdService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 会员模块导出报表
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class CustomerReportExportService {

    @Autowired
    private CustomerOrderReportMapper customerOrderReportMapper;

    @Autowired
    private CustomerGrowthReportMapper customerGrowthReportMapper;

    @Autowired
    private OsdService osdService;

    @Autowired
    private StoreService storeService;

//    @Autowired
//    private CustomerReportGenerateService generateService;

    @Autowired
    private ReplayStoreMapper replayStoreMapper;

    @Autowired
    private AreaDistributeReportMapper areaDistributeReportMapper;

    /**
     * 导出用户报表
     *
     * @param exportQuery exportQuery
     */
    public List<String> exportCustomerReport(ExportQuery exportQuery) throws IOException {
        List<String> fileUrl = Lists.newArrayList();
        if (StringUtils.isBlank(exportQuery.getCompanyId())) {
            exportQuery.setCompanyId(Constants.bossId);
        }
        LocalDate endDate = DateUtil.parse2Date(exportQuery.getDateTo(), DateUtil.FMT_DATE_1);
        //客户名称
        String commonFileName = String.format("customer/order/%s/%s客户订货报表_按客户查看_%s_%s", DateUtil.format(endDate, DateUtil.FMT_MONTH_2),
                storeService.getStoreName(exportQuery), exportQuery.getDateFrom(), exportQuery.getDateTo());
        commonFileName = osdService.getFileRootPath().concat(commonFileName);
        Store store = replayStoreMapper.queryByCompanyInfoId(Long.valueOf(exportQuery.getCompanyId()));
        if(exportQuery.getCompanyId().equals(Constants.bossId)||store.getCompanyType()==0){
            exportQuery.setCompanyId(Constants.bossId);
        }
        CustomerOrderDataRequest customerOrderDataRequest = new CustomerOrderDataRequest();
        customerOrderDataRequest.setBeginDate(DateUtil.parse2Date(exportQuery.getDateFrom(), DateUtil.FMT_DATE_1));
        customerOrderDataRequest.setEndDate( DateUtil.parse2Date(exportQuery.getDateTo(), DateUtil.FMT_DATE_1).plusDays(1));
        int totalNum = customerOrderReportMapper.exportCustomerOrderTotalForBoss(customerOrderDataRequest);
        if(!exportQuery.getCompanyId().equals(Constants.bossId)){
            customerOrderDataRequest.setCompanyInfoId(Integer.valueOf(exportQuery.getCompanyId()));
            totalNum = customerOrderReportMapper.exportCustomerOrderTotalForSupplier(customerOrderDataRequest);
        }
        if (totalNum == 0) {
            String fileName = String.format("%s.xls", commonFileName);
            if (!osdService.existsFiles(fileName)) {
                ExcelHelper excelHelper = new ExcelHelper();
                customerReportSheet(Lists.newArrayList(), excelHelper);
                ByteArrayOutputStream emptyStream = new ByteArrayOutputStream();
                excelHelper.write(emptyStream);
                osdService.uploadExcel(emptyStream, fileName);
            }
            fileUrl.add(fileName);
            return fileUrl;
        }
        int fileSize = calPage(totalNum,exportQuery.getSize());
        for(int i = 0;i<fileSize;i++){
            ExcelHelper excelHelper = new ExcelHelper();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for(int num =0;num<calPage(totalNum-i*exportQuery.getSize(),500);num++){
                customerOrderDataRequest.setNumber((long)num+i*10);
                customerOrderDataRequest.setSize(500L);
                List<CustomerReport> exportCustomerReports = customerOrderReportMapper.exportCustomerOrderForBoss(customerOrderDataRequest);
                if(customerOrderDataRequest.getCompanyInfoId()!=0){
                    exportCustomerReports = customerOrderReportMapper.exportCustomerOrderForSupplier(customerOrderDataRequest);
                }
                customerReportSheet(exportCustomerReports, excelHelper);
                excelHelper.write(byteArrayOutputStream);
                if(num>9){
                    break ;
                }
            }
            String fileName = i == 0 ? String.format("%s.xls", commonFileName) : String.format("%s(%s).xls", commonFileName,
                    String.valueOf(i));
            osdService.uploadExcel(byteArrayOutputStream, fileName);
            fileUrl.add(fileName);
        }
        return fileUrl;
    }

    /**
     * 会员报表构造
     *
     * @param customerReports customerReports
     * @param excelHelper     excelHelper
     */
    private void customerReportSheet(List<CustomerReport> customerReports, ExcelHelper excelHelper) {
        excelHelper.addSheet("客户订货报表-按客户查看", new Column[]{
                new Column("客户名称", new SpelColumnRender<CustomerReport>("name")),
                new Column("客户账号", new SpelColumnRender<CustomerReport>("account")),
                new Column("下单笔数", new SpelColumnRender<CustomerReport>("orderCount")),
                new Column("下单件数", new SpelColumnRender<CustomerReport>("skuCount")),
                new Column("下单金额", (cell, object) -> {
                    CustomerReport customerReport = (CustomerReport) object;
                    cell.setCellValue(customerReport.getAmount().toString());
                }),
                new Column("付款订单数", new SpelColumnRender<CustomerReport>("payOrderCount")),
                new Column("付款金额", (cell, object) -> {
                    CustomerReport customerReport = (CustomerReport) object;
                    cell.setCellValue(customerReport.getPayAmount().toString());
                }),
                new Column("笔单价", (cell, object) -> {
                    CustomerReport customerReport = (CustomerReport) object;
                    if (Objects.nonNull(customerReport.getOrderPerPrice())) {
                        cell.setCellValue(customerReport.getOrderPerPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    } else {
                        cell.setCellValue(BigDecimal.ZERO.setScale(2).toString());
                    }
                }),
                new Column("退单笔数", new SpelColumnRender<CustomerReport>("returnCount")),
                new Column("退货件数", new SpelColumnRender<CustomerReport>("returnSkuCount")),
                new Column("退单金额", (cell, object) -> {
                    CustomerReport customerReport = (CustomerReport) object;
                    cell.setCellValue(customerReport.getReturnAmount().toString());
                })
        }, customerReports);
    }


    /**
     * 导出会员报表-地区维度
     *
     * @param exportQuery
     * @return
     */
    public List<String> exportCustomerAreaReport(ExportQuery exportQuery) throws IOException {
        List<String> fileUrl = Lists.newArrayList();
        if (StringUtils.isBlank(exportQuery.getCompanyId())) {
            exportQuery.setCompanyId(Constants.bossId);
        }
        LocalDate endDate = DateUtil.parse2Date(exportQuery.getDateTo(), DateUtil.FMT_DATE_1);
        //客户名称
        String commonFileName = String.format("customer/order/%s/%s客户订货报表_按地区查看_%s_%s", DateUtil.format(endDate, DateUtil.FMT_MONTH_2),
                storeService.getStoreName(exportQuery), exportQuery.getDateFrom(), exportQuery.getDateTo());
        commonFileName = osdService.getFileRootPath().concat(commonFileName);
        Store store = replayStoreMapper.queryByCompanyInfoId(Long.valueOf(exportQuery.getCompanyId()));
        if(exportQuery.getCompanyId().equals(Constants.bossId)||store.getCompanyType()==0){
            exportQuery.setCompanyId(Constants.bossId);
        }
        CustomerOrderDataRequest customerOrderDataRequest = new CustomerOrderDataRequest();
        customerOrderDataRequest.setBeginDate(DateUtil.parse2Date(exportQuery.getDateFrom(), DateUtil.FMT_DATE_1));
        customerOrderDataRequest.setEndDate( DateUtil.parse2Date(exportQuery.getDateTo(), DateUtil.FMT_DATE_1).plusDays(1));
        customerOrderDataRequest.setShopType(0);
        int totalNum = customerOrderReportMapper.exportCustomerAreaOrderTotalForBoss(customerOrderDataRequest);
        if(!exportQuery.getCompanyId().equals(Constants.bossId)){
            customerOrderDataRequest.setShopType(1);
            customerOrderDataRequest.setCompanyInfoId(Integer.valueOf(exportQuery.getCompanyId()));
            totalNum = customerOrderReportMapper.exportCustomerAreaOrderTotalForSupplier(customerOrderDataRequest);
        }
        if (totalNum == 0) {
            String fileName = String.format("%s.xls", commonFileName);
            if (!osdService.existsFiles(fileName)) {
                ExcelHelper excelHelper = new ExcelHelper();
                customerReportSheet(Lists.newArrayList(), excelHelper);
                ByteArrayOutputStream emptyStream = new ByteArrayOutputStream();
                excelHelper.write(emptyStream);
                osdService.uploadExcel(emptyStream, fileName);
            }
            fileUrl.add(fileName);
            return fileUrl;
        }
        int fileSize = calPage(totalNum,exportQuery.getSize());
        for(int i = 0;i<fileSize;i++){
            ExcelHelper excelHelper = new ExcelHelper();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for(int num =0;num<calPage(totalNum-i*exportQuery.getSize(),500);num++){
                customerOrderDataRequest.setNumber((long)num+i*10);
                customerOrderDataRequest.setSize(500L);
                List<CustomerAreaReport> customerReports = new ArrayList<>();
                if(customerOrderDataRequest.getShopType()==1){
                    customerReports = customerOrderReportMapper.exportCustomerAreaOrderForSupplier(customerOrderDataRequest);
                } else {
                    customerReports = customerOrderReportMapper.exportCustomerAreaOrderForBoss(customerOrderDataRequest);
                }
                customerAreaReportSheet(customerReports, excelHelper);
                excelHelper.write(byteArrayOutputStream);
                if(num>9){
                    break ;
                }
            }
            String fileName = i == 0 ? String.format("%s.xls", commonFileName) : String.format("%s(%s).xls", commonFileName,
                    String.valueOf(i));
            osdService.uploadExcel(byteArrayOutputStream, fileName);
            fileUrl.add(fileName);
        }
        return fileUrl;
    }

    /**
     * 会员地区报表构造
     *
     * @param customerReports customerReports
     * @param excelHelper     excelHelper
     */
    private void customerAreaReportSheet(List<CustomerAreaReport> customerReports, ExcelHelper excelHelper) {
        excelHelper.addSheet("客户订货报表-按地区查看", new Column[]{
                new Column("客户地区", (cell, object) -> {
                    CustomerAreaReport areaReport = (CustomerAreaReport) object;
                    cell.setCellValue(parseAreaName(areaReport.getCityId()));
                }),
                new Column("下单笔数", new SpelColumnRender<CustomerAreaReport>("orderCount")),
                new Column("下单件数", new SpelColumnRender<CustomerAreaReport>("skuCount")),
                new Column("下单金额", (cell, object) -> {
                    CustomerAreaReport areaReport = (CustomerAreaReport) object;
                    cell.setCellValue(areaReport.getAmount().toString());
                }),
                new Column("付款订单数", new SpelColumnRender<CustomerAreaReport>("payOrderCount")),
                new Column("付款金额", (cell, object) -> {
                    CustomerAreaReport areaReport = (CustomerAreaReport) object;
                    cell.setCellValue(areaReport.getPayAmount().toString());
                }),
                new Column("客单价", (cell, object) -> {
                    CustomerAreaReport areaReport = (CustomerAreaReport) object;
                    if (Objects.nonNull(areaReport.getUserPerPrice())) {
                        cell.setCellValue(areaReport.getUserPerPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    } else {
                        cell.setCellValue(BigDecimal.ZERO.setScale(2).toString());
                    }
                }),
                new Column("笔单价", (cell, object) -> {
                    CustomerAreaReport areaReport = (CustomerAreaReport) object;
                    if (Objects.nonNull(areaReport.getOrderPerPrice())) {
                        cell.setCellValue(areaReport.getOrderPerPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    } else {
                        cell.setCellValue(BigDecimal.ZERO.setScale(2).toString());
                    }
                }),
                new Column("退单笔数", new SpelColumnRender<CustomerAreaReport>("returnCount")),
                new Column("退货件数", new SpelColumnRender<CustomerAreaReport>("returnSkuCount")),
                new Column("退单金额", (cell, object) -> {
                    CustomerAreaReport areaReport = (CustomerAreaReport) object;
                    cell.setCellValue(areaReport.getReturnAmount().toString());
                })
        }, customerReports);
    }

    /**
     * 导出等级查看
     *
     * @param exportQuery exportQuery
     * @return
     * @throws IOException
     */
    public List<String> exportCustomerLevelReport(ExportQuery exportQuery) throws IOException {
        List<String> fileUrl = Lists.newArrayList();
        if (StringUtils.isBlank(exportQuery.getCompanyId())) {
            exportQuery.setCompanyId(Constants.bossId);
        }
        LocalDate endDate = DateUtil.parse2Date(exportQuery.getDateTo(), DateUtil.FMT_DATE_1);
        //客户名称
        String commonFileName = String.format("customer/order/%s/%s客户订货报表_按等级查看_%s_%s", DateUtil.format(endDate, DateUtil.FMT_MONTH_2),
                storeService.getStoreName(exportQuery), exportQuery.getDateFrom(), exportQuery.getDateTo());
        commonFileName = osdService.getFileRootPath().concat(commonFileName);
        Store store = replayStoreMapper.queryByCompanyInfoId(Long.valueOf(exportQuery.getCompanyId()));
        if(exportQuery.getCompanyId().equals(Constants.bossId)||store.getCompanyType()==0){
            exportQuery.setCompanyId(Constants.bossId);
        }
        CustomerOrderDataRequest customerOrderDataRequest = new CustomerOrderDataRequest();
        customerOrderDataRequest.setBeginDate(DateUtil.parse2Date(exportQuery.getDateFrom(), DateUtil.FMT_DATE_1));
        customerOrderDataRequest.setEndDate( DateUtil.parse2Date(exportQuery.getDateTo(), DateUtil.FMT_DATE_1).plusDays(1));
        customerOrderDataRequest.setShopType(0);
        int totalNum = customerOrderReportMapper.exportCustomerLevelOrderTotalForBoss(customerOrderDataRequest);
        if(!exportQuery.getCompanyId().equals(Constants.bossId)){
            customerOrderDataRequest.setShopType(1);
            customerOrderDataRequest.setCompanyInfoId(Integer.valueOf(exportQuery.getCompanyId()));
            totalNum = customerOrderReportMapper.exportCustomerLevelOrderTotalForSupplier(customerOrderDataRequest);
        }
        if (totalNum == 0) {
            String fileName = String.format("%s.xls", commonFileName);
            if (!osdService.existsFiles(fileName)) {
                ExcelHelper excelHelper = new ExcelHelper();
                customerReportSheet(Lists.newArrayList(), excelHelper);
                ByteArrayOutputStream emptyStream = new ByteArrayOutputStream();
                excelHelper.write(emptyStream);
                osdService.uploadExcel(emptyStream, fileName);
            }
            fileUrl.add(fileName);
            return fileUrl;
        }
        int fileSize = calPage(totalNum,exportQuery.getSize());
        for(int i = 0;i<fileSize;i++){
            ExcelHelper excelHelper = new ExcelHelper();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for(int num =0;num<calPage(totalNum-i*exportQuery.getSize(),500);num++){
                customerOrderDataRequest.setNumber((long)num+i*10);
                customerOrderDataRequest.setSize(500L);
                List<CustomerLevelReport> customerReports = new ArrayList<>();
                if(customerOrderDataRequest.getShopType()==1){
                    customerReports = customerOrderReportMapper.exportCustomerLevelOrderForSupplier(customerOrderDataRequest);
                } else {
                    customerReports = customerOrderReportMapper.exportCustomerLevelOrderForBoss(customerOrderDataRequest);
                }
                customerLevelSheet(customerReports, excelHelper);
                excelHelper.write(byteArrayOutputStream);
                if(num>9){
                    break ;
                }
            }
            String fileName = i == 0 ? String.format("%s.xls", commonFileName) : String.format("%s(%s).xls", commonFileName,
                    String.valueOf(i));
            osdService.uploadExcel(byteArrayOutputStream, fileName);
            fileUrl.add(fileName);
        }
        return fileUrl;
    }

    /**
     * 会员等级报表构造
     *
     * @param customerReports customerReports
     * @param excelHelper     excelHelper
     */
    private void customerLevelSheet(List<CustomerLevelReport> customerReports, ExcelHelper excelHelper) {
        excelHelper.addSheet("客户订货报表-按等级查看", new Column[]{
                new Column("客户等级", new SpelColumnRender<CustomerLevelReport>("name")),
                new Column("下单笔数", new SpelColumnRender<CustomerLevelReport>("orderCount")),
                new Column("下单件数", new SpelColumnRender<CustomerLevelReport>("skuCount")),
                new Column("下单金额", (cell, object) -> {
                    CustomerLevelReport customerLevelReport = (CustomerLevelReport) object;
                    cell.setCellValue(customerLevelReport.getAmount().toString());
                }),
                new Column("付款订单数", new SpelColumnRender<CustomerLevelReport>("payOrderCount")),
                new Column("付款金额", (cell, object) -> {
                    CustomerLevelReport customerLevelReport = (CustomerLevelReport) object;
                    cell.setCellValue(customerLevelReport.getPayAmount().toString());
                }),
                new Column("客单价", (cell, object) -> {
                    CustomerLevelReport customerLevelReport = (CustomerLevelReport) object;
                    if (Objects.nonNull(customerLevelReport.getUserPerPrice())) {
                        cell.setCellValue(customerLevelReport.getUserPerPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    } else {
                        cell.setCellValue(BigDecimal.ZERO.setScale(2).toString());
                    }
                }),
                new Column("笔单价", (cell, object) -> {
                    CustomerLevelReport levelReport = (CustomerLevelReport) object;
                    if (Objects.nonNull(levelReport.getOrderPerPrice())) {
                        cell.setCellValue(levelReport.getOrderPerPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    } else {
                        cell.setCellValue(BigDecimal.ZERO.setScale(2).toString());
                    }
                }),
                new Column("退单笔数", new SpelColumnRender<CustomerLevelReport>("returnCount")),
                new Column("退货件数", new SpelColumnRender<CustomerLevelReport>("returnSkuCount")),
                new Column("退单金额", (cell, object) -> {
                    CustomerLevelReport customerLevelReport = (CustomerLevelReport) object;
                    cell.setCellValue(customerLevelReport.getReturnAmount().toString());
                })
        }, customerReports);
    }


    /**
     * 导出会员增长
     *
     * @param exportQuery exportQuery
     * @return List<String>
     * @throws IOException
     */
    public List<String> exportCustomerGrowthReport(ExportQuery exportQuery) throws IOException {
        List<String> fileUrl = Lists.newArrayList();
        CustomerGrowthReportRequest reportRequest = new CustomerGrowthReportRequest();
        reportRequest.setCompanyId(exportQuery.getCompanyId() == null ? "0" : exportQuery.getCompanyId());
        reportRequest.setStartDate(exportQuery.getDateFrom()).setEnDate(exportQuery.getDateTo());
        reportRequest.setSortField("base_date");
        reportRequest.setSortTypeText("DESC");
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageSize(exportQuery.getSize());

        boolean isPlatformRequest = exportQuery.getCompanyId() != null && exportQuery.getCompanyId().equals("0");

        int count = customerGrowthReportMapper.countCustomerReport(reportRequest);
        LocalDate endDate = DateUtil.parse2Date(exportQuery.getDateTo(), DateUtil.FMT_DATE_1);
        String commonFileName = String.format("customer/growth/%s/%s客户增长报表_%s_%s", DateUtil.format(endDate, DateUtil.FMT_MONTH_2),
                storeService.getStoreName(exportQuery), exportQuery.getDateFrom(), exportQuery.getDateTo());
        commonFileName = osdService.getFileRootPath().concat(commonFileName);
        if (count < 1) {
            String fileName = String.format("%s.xls", commonFileName);
            //不存在，则上传
            if (!osdService.existsFiles(fileName)) {
                ExcelHelper excelHelper = new ExcelHelper();
                customerGrowthSheet(Lists.newArrayList(), excelHelper, isPlatformRequest);
                ByteArrayOutputStream emptyStream = new ByteArrayOutputStream();
                excelHelper.write(emptyStream);
                osdService.uploadExcel(emptyStream, fileName);
            }
            fileUrl.add(fileName);
            return fileUrl;
        }

        int page = calPage(count, exportQuery.getSize());

        for (int i = 0; i < page; i++) {
            pageRequest.setStart(i * exportQuery.getSize());
            List<CustomerGrowthReport> customerGrowthReports = customerGrowthReportMapper.findAllCustomerGrowReport(reportRequest, pageRequest);
            if (CollectionUtils.isEmpty(customerGrowthReports)) {
                return fileUrl;
            }

            ExcelHelper excelHelper = new ExcelHelper();
            customerGrowthSheet(customerGrowthReports, excelHelper, isPlatformRequest);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            excelHelper.write(byteArrayOutputStream);
            String fileName = i == 0 ? String.format("%s.xls", commonFileName) : String.format("%s(%s).xls", commonFileName,
                    String.valueOf(i));
            osdService.uploadExcel(byteArrayOutputStream, fileName);
            fileUrl.add(fileName);
        }

        return fileUrl;
    }

    /**
     * 会员增长报表格式
     *
     * @param customerGrowthReports customerGrowthReports
     * @param excelHelper           excelHelper
     */
    private void customerGrowthSheet(List<CustomerGrowthReport> customerGrowthReports, ExcelHelper excelHelper, boolean isPlatformRequest) {
        if (isPlatformRequest) {
            excelHelper.addSheet("客户增长报表", new Column[]{
                    new Column("日期", new SpelColumnRender<CustomerGrowthReport>("baseDate")),
                    new Column("客户总数", new SpelColumnRender<CustomerGrowthReport>("customerAllCount")),
                    new Column("新增客户数", new SpelColumnRender<CustomerGrowthReport>("customerDayGrowthCount")),
                    new Column("注册客户数", new SpelColumnRender<CustomerGrowthReport>("customerDayRegisterCount")),
            }, customerGrowthReports);
        } else {
            excelHelper.addSheet("客户增长报表", new Column[]{
                    new Column("日期", new SpelColumnRender<CustomerGrowthReport>("baseDate")),
                    new Column("客户总数", new SpelColumnRender<CustomerGrowthReport>("customerAllCount")),
                    new Column("新增客户数", new SpelColumnRender<CustomerGrowthReport>("customerDayGrowthCount")),
            }, customerGrowthReports);
        }
    }

    /**
     * 计算页码
     *
     * @param count
     * @param size
     * @return
     */
    private int calPage(int count, int size) {
        int page = count / size;
        if (count % size == 0) {
            return page;
        } else {
            return page + 1;
        }
    }

    private static List<String> specialRegion = Lists.newArrayList("810000", "820000", "710000");


    /**
     * 获取省市区名字
     *
     * @param cityCode cityCode
     * @return name
     */
    private String parseAreaName(String cityCode) {
        String areaName = "";
        if (specialRegion.contains(cityCode)) {
            switch (cityCode) {
                case "810000":
                    areaName = "香港特别行政区";
                    break;
                case "820000":
                    areaName = "澳门特别行政区";
                    break;
                case "710000":
                    areaName = "台湾省";
                    break;
            }
        } else if(cityCode.equals("-1")){
            areaName = "其他";
        }else {
            City city = areaDistributeReportMapper.queryCityInfo(cityCode);
            if (Objects.nonNull(city)) {
                Province province = areaDistributeReportMapper.queryProvinceInfo(cityCode);
                if (Objects.nonNull(province)) {
                    areaName = ObjectUtils.toString(province.getName()).concat("/").concat(ObjectUtils.toString(city.getName()));
                }
            }
        }
        if (StringUtils.isBlank(areaName)) {
            areaName = "其他";
        }
        return areaName;
    }
}
