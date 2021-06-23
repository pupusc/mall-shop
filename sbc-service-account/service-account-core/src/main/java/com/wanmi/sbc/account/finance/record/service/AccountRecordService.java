package com.wanmi.sbc.account.finance.record.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.*;
import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.account.api.request.finance.record.AccountDetailsExportRequest;
import com.wanmi.sbc.account.api.request.finance.record.AccountDetailsPageRequest;
import com.wanmi.sbc.account.api.request.finance.record.AccountRecordPageRequest;
import com.wanmi.sbc.account.api.request.finance.record.AccountRecordToExcelRequest;
import com.wanmi.sbc.account.bean.enums.PayWay;
import com.wanmi.sbc.account.finance.record.model.entity.PayItemRecord;
import com.wanmi.sbc.account.finance.record.model.entity.PaySummarize;
import com.wanmi.sbc.account.finance.record.model.entity.Reconciliation;
import com.wanmi.sbc.account.finance.record.model.entity.TotalRecord;
import com.wanmi.sbc.account.finance.record.model.response.*;
import com.wanmi.sbc.account.finance.record.repository.ReconciliationRepository;
import com.wanmi.sbc.account.finance.record.utils.FinanceUtils;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.ListStoreByIdsRequest;
import com.wanmi.sbc.customer.api.request.store.ListStoreByNameRequest;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.hibernate.Criteria;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import sun.misc.BASE64Encoder;

import javax.persistence.criteria.Predicate;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.wanmi.sbc.common.util.DateUtil.FMT_TIME_1;

/**
 * <p>财务对账单Service</p>
 * Created by of628-wenzhi on 2017-12-08-上午10:35.
 */
@Service
@Slf4j
public class AccountRecordService {

    @Value("classpath:accountRecord.xlsx")
    private Resource templateFile;

    @Autowired
    private ReconciliationRepository repository;

//    @Autowired
//    private StoreService storeService;

    @Autowired
    private GeneratorService generatorService;
    @Autowired
    private StoreQueryProvider storeQueryProvider;

    public static final String EXCEL_NAME = "财务对账";

    public static final String EXCEL_TYPE = "xlsx";

    /**
     * 对账单列表分页查询
     *
     * @param request 分页查询参数结构
     * @param type    0：收入 1：退款
     * @return 分页后的对账单列表
     */
//    public Page<AccountRecord> page(AccountRecordPageRequest request, Byte type) {
//        PageRequest pageable = new PageRequest(request.getPageNum(), request.getPageSize());
//        LocalDateTime beginTime = DateUtil.parse(request.getBeginTime(), FMT_TIME_1);
//        LocalDateTime endTime = DateUtil.parse(request.getEndTime(), FMT_TIME_1);
//        List<Long> storeIds = new ArrayList<>();
//        if (StringUtils.isNotBlank(request.getKeywords())) {
//            List<Store> stores = storeService.queryStoreByName(request.getKeywords());
//            if (CollectionUtils.isNotEmpty(stores)) {
//                storeIds = stores.stream().mapToLong(Store::getStoreId).boxed().collect(Collectors.toList());
//            } else {
//                return new PageImpl<>(Collections.emptyList(), pageable, 0);
//            }
//        }
//        //先根据商家和店铺分页抓取总金额
//        Page<TotalRecord> pageRecord = repository.queryTotalRecord(beginTime, endTime, request.getSupplierId(),
//                storeIds.size(), storeIds.isEmpty() ? Collections.singletonList(0L) : storeIds, type, pageable
//        );
//        List<AccountRecord> records = new ArrayList<>();
//        List<TotalRecord> content = pageRecord.getContent();
//        boolean flag = type == 1;
//        if (!content.isEmpty()) {
//            List<PayWay> payWays = Arrays.stream(PayWay.values()).collect(Collectors.toList());
//            storeIds = content.stream().mapToLong(TotalRecord::getStoreId).boxed().collect(Collectors.toList());
//            Map<Long, Store> storeMap = storeService.findAllList(storeIds).stream().collect(
//                    Collectors.toMap(Store::getStoreId, Function.identity())
//            );
//            //根据当前分页抓取不同支付方式的对应金额
//            List<PayItemRecord> payItemRecords = repository.queryPayItemRecord(beginTime, endTime, storeIds, type);
//            //merge
//            records = IteratorUtils.zip(content, payItemRecords, (a, b) -> Objects.equals(a.getStoreId(), b
//                            .getStoreId()),
//                    (total, items) -> {
//                        String prefix = flag ? "-￥" : "￥";
//                        AccountRecord record = new AccountRecord();
//                        Store store = storeMap.remove(total.getStoreId());
//                        record.setStoreId(total.getStoreId());
//                        record.setStoreName(store.getStoreName());
//                        record.setSupplierId(total.getSupplierId());
//                        record.setSupplierName(store.getSupplierName());
//                        record.setTotalAmount(prefix.concat(FinanceUtils.amountFormatter(total.getTotalAmount())));
//                        Map<String, String> payItemAmountMap = items.stream().collect(
//                                Collectors.toMap(k -> k.getPayWay().toValue(), k -> prefix.concat(FinanceUtils
//                                        .amountFormatter(k.getAmount()))));
//                        record.setPayItemAmountMap(payItemAmountMap);
//                        payWays.forEach(p -> {
//                            if (!payItemAmountMap.containsKey(p.toValue())) {
//                                record.getPayItemAmountMap().put(p.toValue(), prefix.concat("0.00"));
//                            }
//                        });
//                        return record;
//                    });
//
//        }
//        return new PageImpl<>(records, pageable, pageRecord.getTotalElements());
//    }

    /**
     * 对账单列表分页查询
     *
     * @param request 分页查询参数结构
     * @param type    0：收入 1：退款
     * @return 分页后的对账单列表
     */
    public Page<AccountRecord> page(AccountRecordPageRequest request, Byte type) {
        PageRequest pageable =  PageRequest.of(request.getPageNum(), request.getPageSize());
        LocalDateTime beginTime = DateUtil.parse(request.getBeginTime(), FMT_TIME_1);
        LocalDateTime endTime = DateUtil.parse(request.getEndTime(), FMT_TIME_1);
        List<Long> storeIds = new ArrayList<>();
        if (StringUtils.isNotBlank(request.getKeywords())) {
            List<StoreVO> stores = storeQueryProvider.listByName(new ListStoreByNameRequest(request.getKeywords()))
                    .getContext().getStoreVOList();
            if (CollectionUtils.isNotEmpty(stores)) {
                storeIds = stores.stream().mapToLong(StoreVO::getStoreId).boxed().collect(Collectors.toList());
            } else {
                return new PageImpl<>(Collections.emptyList(), pageable, 0);
            }
        }
        //先根据商家和店铺分页抓取总金额
        Page<TotalRecord> pageRecord = repository.queryTotalRecord(beginTime, endTime, request.getSupplierId(),
                storeIds.size(), storeIds.isEmpty() ? Collections.singletonList(0L) : storeIds, type, pageable
        );
        List<AccountRecord> records = new ArrayList<>();
        List<TotalRecord> content = pageRecord.getContent();
        boolean flag = type == 1;
        if (!content.isEmpty()) {
            List<PayWay> payWays = Arrays.stream(PayWay.values()).collect(Collectors.toList());
            storeIds = content.stream().mapToLong(TotalRecord::getStoreId).boxed().collect(Collectors.toList());
            Map<Long, StoreVO> storeMap = storeQueryProvider.listByIds(new ListStoreByIdsRequest(storeIds)).getContext
                    ().getStoreVOList().stream()
                    .collect(
                            Collectors.toMap(StoreVO::getStoreId, Function.identity())
                    );
            //根据当前分页抓取不同支付方式的对应金额
            List<PayItemRecord> payItemRecords = repository.queryPayItemRecord(beginTime, endTime, storeIds, type);
            //merge
            records = IteratorUtils.zip(content, payItemRecords, (a, b) -> Objects.equals(a.getStoreId(), b
                            .getStoreId()),
                    (total, items) -> {
                        String prefix = flag ? "-￥" : "￥";
                        AccountRecord record = new AccountRecord();
                        StoreVO store = storeMap.remove(total.getStoreId());
                        record.setStoreId(total.getStoreId());
                        record.setStoreName(store.getStoreName());
                        record.setSupplierId(total.getSupplierId());
                        record.setSupplierName(store.getSupplierName());
                        record.setTotalAmount(prefix.concat(FinanceUtils.amountFormatter(total.getTotalAmount())));
                        Map<String, String> payItemAmountMap = items.stream().collect(
                                Collectors.toMap(k -> k.getPayWay().toValue(), k -> prefix.concat(FinanceUtils
                                        .amountFormatter(k.getAmount()))));
                        record.setPayItemAmountMap(payItemAmountMap);
                        payWays.forEach(p -> {
                            if (!payItemAmountMap.containsKey(p.toValue())) {
                                record.getPayItemAmountMap().put(p.toValue(), prefix.concat("0.00"));
                            }
                        });
                        return record;
                    });

        }
        return new PageImpl<>(records, pageable, pageRecord.getTotalElements());
    }


    /**
     * 对账列表汇总
     *
     * @param request 请求参数
     * @param type    0：收入 1：退款
     * @return 返回 对象列表汇总
     */
    public List<AccountGather> summarizing(AccountRecordPageRequest request, Byte type) {
        LocalDateTime beginTime = DateUtil.parse(request.getBeginTime(), FMT_TIME_1);
        LocalDateTime endTime = DateUtil.parse(request.getEndTime(), FMT_TIME_1);
        List<PaySummarize> summarizes = repository.summarizing(beginTime, endTime, request.getSupplierId(), type);
        BigDecimal sum = summarizes.stream().map(PaySummarize::getSumAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        Map<String, PaySummarize> summarizeMap = summarizes.stream().collect(Collectors.toMap(p -> p.getPayWay()
                        .toValue(),
                Function.identity()));
        return Arrays.stream(PayWay.values()).map(
                (PayWay payWay) -> {
                    AccountGather gather = new AccountGather();
                    gather.setPayWay(payWay);
                    PaySummarize data = summarizeMap.remove(payWay.toValue());
                    if (!Objects.isNull(data)) {
                        NumberFormat fmt = NumberFormat.getPercentInstance();
                        //设置百分数精确度2即保留两位小数
                        fmt.setMinimumFractionDigits(2);
                        gather.setPercentage(fmt.format(BigDecimal.ZERO.compareTo(sum) != 0 ? data.getSumAmount().divide(sum, 6, RoundingMode.HALF_UP)
                                .doubleValue() : 0));
                        gather.setSumAmount("￥".concat(FinanceUtils.amountFormatter(data.getSumAmount())));
                    } else {
                        gather.setPercentage("0.00%");
                        gather.setSumAmount("￥0.00");
                    }
                    return gather;
                }
        ).collect(Collectors.toList());
    }

    /**
     * 分页查询收入/退款明细
     *
     * @param request 分页查询参数结构
     * @param type    0：收入 1：退款
     * @return 分页后的对账明细列表
     */
    public Page<AccountDetails> pageDetails(AccountDetailsPageRequest request, Byte type) {
        PageRequest pageable =  PageRequest.of(request.getPageNum(), request.getPageSize());
        request.setType(Integer.valueOf(type));
        Page<Reconciliation> results =  repository.findAll(
                ReconciliationWhereCriteriaBuilder.build(request),
                request.getPageRequest());
        List<Reconciliation> content = results.getContent();
        List<AccountDetails> accountDetails = new ArrayList<>();
        wraperAccountDetails(type, request.getStoreId(), content, accountDetails, true);
        return new PageImpl<>(accountDetails, pageable, results.getTotalElements());

    }

    /**
     * 查询需要导出的收入/退款明细
     *
     * @param request 分页查询参数结构
     * @param type    0：收入 1：退款
     * @return 分页后的对账明细列表
     */
    public List<AccountDetails> exportDetailsLoad(AccountDetailsExportRequest request, Byte type) {
        LocalDateTime beginTime = DateUtil.parse(request.getBeginTime(), FMT_TIME_1);
        LocalDateTime endTime = DateUtil.parse(request.getEndTime(), FMT_TIME_1);
        List<Reconciliation> detailList = repository.queryDetails(request.getStoreId(), beginTime, endTime, type,
                request.getPayWay(), request.getTradeNo());
        List<AccountDetails> accountDetails = new ArrayList<>();
        wraperAccountDetails(type, request.getStoreId(), detailList, accountDetails, false);
        return accountDetails;
    }

//    TODO:导出迁移至BFF下AccountRecordController
//     /**
//     * 导出收入明细
//     *
//     * @param details      收入明细列表
//     * @param outputStream OutputStream
//     */
//    @SuppressWarnings("unchecked")
//    public void doExportIncomeDetails(List<AccountDetails> details, ServletOutputStream outputStream) {
//        ExcelHelper helper = new ExcelHelper();
//        helper.addSheet("对账单收入明细", new Column[]{
//                new Column("下单时间", new SpelColumnRender<AccountDetails>("orderTime")),
//                new Column("订单编号", new SpelColumnRender<AccountDetails>("orderCode")),
//                new Column("客户昵称", new SpelColumnRender<AccountDetails>("customerName")),
//                new Column("支付时间", new SpelColumnRender<AccountDetails>("tradeTime")),
//                new Column("支付渠道", (cell, object) -> {
//                    AccountDetails d = (AccountDetails) object;
//                    cell.setCellValue(d.getPayWay().getDesc());
//                }),
//                new Column("支付金额", new SpelColumnRender<AccountDetails>("amount"))
//        }, details);
//        helper.write(outputStream);
//    }

//    /**
//     * 导出退款明细
//     *
//     * @param details      退款明细列表
//     * @param outputStream OutputStream
//     */
//    @SuppressWarnings("unchecked")
//    public void doExportRefundDetails(List<RefundDetails> details, ServletOutputStream outputStream) {
//        ExcelHelper helper = new ExcelHelper();
//        helper.addSheet("对账单退款明细", new Column[]{
//                new Column("退单时间", new SpelColumnRender<RefundDetails>("orderTime")),
//                new Column("退单编号", new SpelColumnRender<RefundDetails>("returnOrderCode")),
//                new Column("订单编号", new SpelColumnRender<AccountDetails>("orderCode")),
//                new Column("客户昵称", new SpelColumnRender<RefundDetails>("customerName")),
//                new Column("退款时间", new SpelColumnRender<RefundDetails>("tradeTime")),
//                new Column("退款渠道", (cell, object) -> {
//                    RefundDetails d = (RefundDetails) object;
//                    cell.setCellValue(d.getPayWay().getDesc());
//                }),
//                new Column("退款金额", new SpelColumnRender<AccountDetails>("amount"))
//        }, details);
//        helper.write(outputStream);
//    }

    /**
     * 根据订单号和交易类型删除
     *
     * @param orderCode 订单编号
     * @param typeFlag  交易类型
     */
    @Transactional
    public void deleteByOrderCodeAndType(String orderCode, Byte typeFlag) {
        repository.deleteByOrderCodeAndType(orderCode, typeFlag);
    }

    /**
     * 根据退单号和交易类型删除
     *
     * @param returnOrderCode 订单编号
     * @param typeFlag        交易类型
     */
    @Transactional
    public void deleteByReturnOrderCodeAndType(String returnOrderCode, Byte typeFlag) {
        repository.deleteByReturnOrderCodeAndType(returnOrderCode, typeFlag);
    }

    /**
     * 新增对账单
     *
     * @param reconciliation 对账单
     */
    @Transactional
    @GlobalTransactional
    public void add(Reconciliation reconciliation) {
        reconciliation.setId(generatorService.generateRNid());
        repository.save(reconciliation);
    }

    /**
     * 批量转换Reconciliation实例转化为AccountDetails实例
     *
     * @param type           交易类型
     * @param storeId        店铺Id
     * @param detailList     AccountDetails新实例
     * @param accountDetails Reconciliation实例
     * @param flag           是否增加前缀￥
     */
    private void wraperAccountDetails(Byte type, Long storeId, List<Reconciliation> detailList,
                                      List<AccountDetails> accountDetails, boolean flag) {
        StoreVO store = storeQueryProvider.getById(new StoreByIdRequest(storeId)).getContext().getStoreVO();
        if (!detailList.isEmpty()) {
            detailList.forEach(
                    i -> {
                        AccountDetails details;
                        if (type == 0) {
                            details = new AccountDetails();

                        } else {
                            details = new RefundDetails();
                        }
                        BeanUtils.copyProperties(i, details);
                        String amount = FinanceUtils.amountFormatter(i.getAmount());
                        details.setAmount(flag ? "￥".concat(amount) : amount);
                        details.setStoreName(store.getStoreName());
                        accountDetails.add(details);
                    }
            );
        }
    }

    /**
     * 设置request查询参数
     *
     * @param request
     * @param type
     * @return
     */
    private AccountRecordPageRequest setRequest(AccountRecordPageRequest request, Byte type) {
        LocalDateTime beginTime = DateUtil.parse(request.getBeginTime(), FMT_TIME_1);
        LocalDateTime endTime = DateUtil.parse(request.getEndTime(), FMT_TIME_1);
        List<Long> storeIds = new ArrayList<>();
        if (StringUtils.isNotBlank(request.getKeywords())) {
            List<StoreVO> stores = storeQueryProvider.listByName(new ListStoreByNameRequest(request.getKeywords()))
                    .getContext().getStoreVOList();
            if (CollectionUtils.isNotEmpty(stores)) {
                storeIds = stores.stream().mapToLong(StoreVO::getStoreId).boxed().collect(Collectors.toList());
            } else {
                return null;
            }
        }
        int total = repository.queryCount(beginTime, endTime, request.getSupplierId(),
                storeIds.size(), storeIds.isEmpty() ? Collections.singletonList(0L) : storeIds, type);
        request.setPageNum(0);
        request.setPageSize(total);
        return request;
    }

    /**
     * 得到财务对账Excel数据
     *
     * @param request
     * @return
     */
    private AccountRecordExcel getExcelData(AccountRecordPageRequest request) {
        String theme = getExcelTheme(request.getBeginTime(), request.getEndTime());
        request = setRequest(request, (byte) 0);
        List<AccountRecord> accountRecords;
        List<AccountGather> accountGathers;
        if (ObjectUtils.isEmpty(request) || request.getPageSize() == 0) {
            accountRecords = Collections.emptyList();
        } else {
            Page<AccountRecord> page = page(request, (byte) 0);
            accountRecords = page.getContent();
        }
        accountGathers = summarizing(request, (byte) 0);
        AccountRecordExcel excelData = new AccountRecordExcel();
        excelData.setAccountGathers(accountGathers);
        excelData.setAccountRecords(accountRecords);
        excelData.setTheme(theme);
        request = setRequest(request, (byte) 1);
        List<AccountRecord> returnRecords;
        List<AccountGather> returnGathers;
        if (ObjectUtils.isEmpty(request) || request.getPageSize() == 0) {
            returnRecords = Collections.emptyList();
        } else {
            Page<AccountRecord> page = page(request, (byte) 1);
            returnRecords = page.getContent();
        }
        returnGathers = summarizing(request, (byte) 1);
        excelData.setReturnRecords(returnRecords);
        excelData.setReturnGathers(returnGathers);
        return excelData;
    }

    /**
     * 将对账数据写入excel
     *
     * @param request
     * @throws Exception
     */
    public String writeAccountRecordToExcel(AccountRecordToExcelRequest request) {
        AccountRecordPageRequest pageRequest = new AccountRecordPageRequest();
        KsBeanUtil.copyPropertiesThird(request, pageRequest);
        AccountRecordExcel excelData = getExcelData(pageRequest);

//        String fileName =  excelData.getTheme()  + "." + EXCEL_TYPE;
//        fileName = URLEncoder.encode(fileName,"UTF-8");
        //写入到response
//        response.setContentType("application/vnd.ms-excel");
//        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

//        OutputStream outputStream = null;
        try (InputStream inputStream = templateFile.getInputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        ) {
            XSSFCellStyle xssfCellStyle = workbook.createCellStyle();
            xssfCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            writeExcel(workbook, excelData, xssfCellStyle);
            workbook.setActiveSheet(request.getAccountRecordType() == null ? 0 : request.getAccountRecordType().toValue());
            workbook.write(baos);
            return new BASE64Encoder().encode(baos.toByteArray());
        } catch (Exception e) {
            log.error("导出Excel错误:" + e);
            throw new SbcRuntimeException(CommonErrorCode.FAILED, e);
        }
    }

    /**
     * 财务对账获取excel主题
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    private String getExcelTheme(String beginTime, String endTime) {
        String theme = EXCEL_NAME + beginTime.substring(0, 10) + "～" + endTime.substring
                (0, 10);
        return theme;
    }

    /**
     * 写入Excel标题
     *
     * @param xssfSheet
     * @param theme
     */
    private void writeExcelTheme(XSSFSheet xssfSheet, String theme) {
        XSSFRow xssfRow = getRow(xssfSheet, 0);
        XSSFCell cell = getCell(xssfRow, 0);
        cell.setCellValue(theme);
    }

    /**
     * 对账数据写入具体操作
     *
     * @param xssfWorkbook
     * @param excelData
     * @throws Exception
     */
    private void writeExcel(XSSFWorkbook xssfWorkbook, AccountRecordExcel excelData, XSSFCellStyle xssfCellStyle)
            throws Exception {
        //收入汇总sheet
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
        //写入标题
        writeExcelTheme(xssfSheet, excelData.getTheme());
        //写入所有店铺收入汇总
        writeExcelTotal(xssfSheet, excelData.getAccountGathers(), xssfCellStyle, (byte) 0);
        //写入各店铺收入明细汇总
        writeExcelStore(xssfSheet, excelData.getAccountRecords(), xssfCellStyle, (byte) 0);

        //退款汇总sheet
        xssfSheet = xssfWorkbook.getSheetAt(1);
        //写入标题
        writeExcelTheme(xssfSheet, excelData.getTheme());
        //写入所有店铺退款汇总
        writeExcelTotal(xssfSheet, excelData.getReturnGathers(), xssfCellStyle, (byte) 1);
        //写入各店铺退款明细汇总
        writeExcelStore(xssfSheet, excelData.getReturnRecords(), xssfCellStyle, (byte) 1);
    }

    /**
     * 写入对账汇总信息
     *
     * @param xssfSheet
     * @param accountGathers
     * @param xssfCellStyle
     * @param type
     */
    private void writeExcelTotal(XSSFSheet xssfSheet, List<AccountGather> accountGathers, XSSFCellStyle xssfCellStyle, Byte type) {
        accountGathers = filterGather(accountGathers, type);
        XSSFRow xssfRow = getRow(xssfSheet, 2);
        for (int cellNum = 0; cellNum < accountGathers.size(); cellNum++) {
            XSSFCell cell = getCell(xssfRow, cellNum + 1);
            cell.setCellStyle(xssfCellStyle);
            String amount = accountGathers.get(cellNum).getSumAmount();
            cell.setCellValue(StringUtils.defaultString(amount));
        }
        xssfRow = getRow(xssfSheet, 3);
        for (int cellNum = 0; cellNum < accountGathers.size(); cellNum++) {
            XSSFCell cell = getCell(xssfRow, cellNum + 1);
            cell.setCellStyle(xssfCellStyle);
            String percentage = accountGathers.get(cellNum).getPercentage();
            cell.setCellValue(StringUtils.defaultString(percentage));
        }
    }

    /**
     * 过滤对账汇总信息
     *
     * @param accountGathers
     * @param type
     * @return
     */
    private List<AccountGather> filterGather(List<AccountGather> accountGathers, Byte type) {
        return accountGathers.stream().filter(accountGather -> (!(StringUtils.equals(accountGather.getPayWay()
                .toValue(), PayWay.ADVANCE.name()))) &&
                (!(StringUtils.equals(accountGather.getPayWay().toValue(), PayWay.COUPON.name()))) &&
                (type != 1 || !(StringUtils.equals(accountGather.getPayWay().toValue(), PayWay.POINT.name())))).collect(Collectors.toList());
    }

    /**
     * 写入各店铺汇总数据
     *
     * @param xssfSheet
     * @param accountRecords
     * @param xssfCellStyle
     * @param type
     */
    private void writeExcelStore(XSSFSheet xssfSheet, List<AccountRecord> accountRecords, XSSFCellStyle xssfCellStyle, Byte type) {
        for (int rowNum = 0; rowNum < accountRecords.size(); rowNum++) {
            //从第7行开始写入
            XSSFRow xssfRow = getRow(xssfSheet, rowNum + 6);
            AccountRecord accountRecord = accountRecords.get(rowNum);
            List<String> recordList = converRecord(accountRecord, rowNum, type);
            for (int cellNum = 0; cellNum < recordList.size(); cellNum++) {
                XSSFCell cell = getCell(xssfRow, cellNum);
                cell.setCellStyle(xssfCellStyle);
                cell.setCellValue(StringUtils.defaultString(recordList.get(cellNum)));
            }
        }
    }

    /**
     * 对record对象转换
     *
     * @param accountRecord
     * @param rowNum
     * @param type
     * @return
     */
    private List<String> converRecord(AccountRecord accountRecord, Integer rowNum, Byte type) {
        if (ObjectUtils.isEmpty(accountRecord)) {
            return Collections.emptyList();
        }
        List<String> recordList = new ArrayList<>();
        recordList.add(String.valueOf(rowNum + 1));
        recordList.add(accountRecord.getStoreName());
        recordList.add(accountRecord.getPayItemAmountMap().get(PayWay.CASH.name()));
        recordList.add(accountRecord.getPayItemAmountMap().get(PayWay.UNIONPAY.name()));
        recordList.add(accountRecord.getPayItemAmountMap().get(PayWay.ALIPAY.name()));
        recordList.add(accountRecord.getPayItemAmountMap().get(PayWay.WECHAT.name()));
        recordList.add(accountRecord.getPayItemAmountMap().get(PayWay.UNIONPAY_B2B.name()));
        if (type == 0) {
            recordList.add(accountRecord.getPayItemAmountMap().get(PayWay.POINT.name()));
        }
        recordList.add(accountRecord.getPayItemAmountMap().get(PayWay.BALANCE.name()));
        recordList.add(accountRecord.getTotalAmount());
        return recordList;
    }

    /**
     * 得到Excel行，防止空指针
     *
     * @param xssfSheet
     * @param rowNum
     * @return
     */
    private XSSFRow getRow(XSSFSheet xssfSheet, int rowNum) {
        XSSFRow xssfRow = xssfSheet.getRow(rowNum);
        if (ObjectUtils.isEmpty(xssfRow)) {
            xssfRow = xssfSheet.createRow(rowNum);
        }
        return xssfRow;
    }

    /**
     * 得到Excel列，防止空指针
     *
     * @param xssfRow
     * @param cellNum
     * @return
     */
    private XSSFCell getCell(XSSFRow xssfRow, int cellNum) {
        XSSFCell cell = xssfRow.getCell(cellNum);
        if (ObjectUtils.isEmpty(cell)) {
            cell = xssfRow.createCell(cellNum);
        }
        return cell;
    }
}
