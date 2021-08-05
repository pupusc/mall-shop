package com.wanmi.sbc.finance;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.account.api.provider.finance.record.AccountRecordProvider;
import com.wanmi.sbc.account.api.provider.finance.record.AccountRecordQueryProvider;
import com.wanmi.sbc.account.api.request.finance.record.*;
import com.wanmi.sbc.account.bean.enums.AccountRecordType;
import com.wanmi.sbc.account.bean.enums.PayWay;
import com.wanmi.sbc.account.bean.vo.AccountDetailsVO;
import com.wanmi.sbc.account.bean.vo.AccountGatherVO;
import com.wanmi.sbc.account.bean.vo.AccountRecordExcelVO;
import com.wanmi.sbc.account.bean.vo.AccountRecordVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.order.api.provider.refund.RefundOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.refund.RefundOrderAccountRecordRequest;
import com.wanmi.sbc.order.api.request.refund.RefundOrderByReturnOrderCodeRequest;
import com.wanmi.sbc.order.api.request.refund.RefundOrderWithoutPageRequest;
import com.wanmi.sbc.order.api.request.trade.TradeAccountRecordRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdsRequest;
import com.wanmi.sbc.order.api.response.refund.RefundOrderAccountRecordResponse;
import com.wanmi.sbc.order.api.response.refund.RefundOrderByReturnCodeResponse;
import com.wanmi.sbc.order.api.response.refund.RefundOrderWithoutPageResponse;
import com.wanmi.sbc.order.api.response.trade.TradeAccountRecordResponse;
import com.wanmi.sbc.order.api.response.trade.TradeGetByIdsResponse;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>财务对账记录Rest</p>
 * Created by of628-wenzhi on 2017-12-12-上午11:13.
 */
@Api(tags = "AccountRecordController", description = "财务对账记录 Api")
@RestController
@RequestMapping("/finance/bill")
@Validated
@Slf4j
public class AccountRecordController {

    @Autowired
    private AccountRecordQueryProvider accountRecordQueryProvider;

    @Resource
    private AccountRecordProvider accountRecordProvider;

    @Resource
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private RefundOrderQueryProvider refundOrderQueryProvider;


    @Resource
    private CommonUtil commonUtil;

    public static final String EXCEL_NAME = "财务对账";

    public static final String EXCEL_TYPE = "xlsx";

    public static final String _AMOUNT_PATTERN_STYLE_NORM = "#,##0.00";


    @Value("classpath:accountRecord.xlsx")
    private org.springframework.core.io.Resource templateFile;



    /**
     * 收入列表
     *
     * @param request 请求参数结构
     * @return 分页后的列表
     */
    @ApiOperation(value = "收入列表")
    @RequestMapping(value = "/income", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<AccountRecordVO>> pageIncome(@RequestBody @Valid
                                                                              AccountRecordPageRequest
                                                                              request) {
        request.setSupplierId(commonUtil.getCompanyInfoId());
        request.setAccountRecordType(AccountRecordType.INCOME);


        MicroServicePage<AccountRecordVO> accountRecordVOPage=  accountRecordQueryProvider.pageAccountRecord(request).getContext()
                .getAccountRecordVOPage();

        //根据开始时间和结束时间，查询店铺下面的积分总额和积分数量

        TradeAccountRecordResponse tradeAccountRecordResponse= tradeQueryProvider.getTradeAccountRecord(TradeAccountRecordRequest.builder().beginTime(request.getBeginTime()).endTime(request.getEndTime()).companyInfoId(commonUtil.getStoreId()).build()).getContext();

        if (CollectionUtils.isNotEmpty(accountRecordVOPage.getContent()) && Objects.nonNull(tradeAccountRecordResponse)) {
            AccountRecordVO accountRecordVO=  accountRecordVOPage.getContent().get(0);
            Map<String, String> payItemAmountMap = accountRecordVO.getPayItemAmountMap();
            payItemAmountMap.put("POINTS_USED","￥".concat(amountFormatter(tradeAccountRecordResponse.getPointsPrice())));
            accountRecordVO.setPoints(tradeAccountRecordResponse.getPoints());
        }
        return BaseResponse.success(accountRecordVOPage);
    }

    /**
     * 收入支付方式汇总
     *
     * @param request 请求参数结构
     * @return 各支付方式金额汇总记录
     */
    @ApiOperation(value = "收入支付方式汇总")
    @RequestMapping(value = "/income/gross", method = RequestMethod.POST)
    public BaseResponse<List<AccountGatherVO>> incomeSummarizing(@RequestBody @Valid AccountGatherListRequest request) {
        request.setSupplierId(commonUtil.getCompanyInfoId());
        request.setAccountRecordType(AccountRecordType.INCOME);

        List<AccountGatherVO> accountGatherVOList= accountRecordQueryProvider.listAccountGather(request).getContext().getAccountGatherVOList();


        log.info("=======收入支付方式汇总:{}======",accountGatherVOList);

        //根据开始时间和结束时间，查询店铺下面的积分总额和积分数量
        TradeAccountRecordResponse tradeAccountRecordResponse= tradeQueryProvider.getTradeAccountRecord(TradeAccountRecordRequest.builder().beginTime(request.getBeginTime()).endTime(request.getEndTime()).companyInfoId(commonUtil.getStoreId()).build()).getContext();

        if (CollectionUtils.isNotEmpty(accountGatherVOList) && Objects.nonNull(tradeAccountRecordResponse)) {

            BigDecimal sum=BigDecimal.ZERO;

            for(AccountGatherVO accountGatherVO:accountGatherVOList){
                String regEx="[\n`~￥？]";
                String newSumAmount = accountGatherVO.getSumAmount().replaceAll(regEx,"");
                BigDecimal sumAmount = new BigDecimal(newSumAmount);
                log.info("=======收入支付和sumAmount:{}======",sumAmount);
                if (accountGatherVO.getPayWay()!=PayWay.POINTS_USED) {
                    sum=sum.add(sumAmount);
                }
            }
            sum=sum.add(tradeAccountRecordResponse.getPointsPrice());

            NumberFormat fmt = NumberFormat.getPercentInstance();
            //设置百分数精确度2即保留两位小数
            fmt.setMinimumFractionDigits(2);


            log.info("=======收入支付和:{}======",sum);

            for(AccountGatherVO accountGatherVO:accountGatherVOList){
                if (accountGatherVO.getPayWay()!=PayWay.POINTS_USED || accountGatherVO.getPayWay()!=PayWay.POINT) {
                    String regEx="[\n`~￥？]";
                    String newSumAmount = accountGatherVO.getSumAmount().replaceAll(regEx,"");
                    BigDecimal sumAmount = new BigDecimal(newSumAmount);
                    accountGatherVO.setPercentage(fmt.format(BigDecimal.ZERO.compareTo(sum) != 0 ? sumAmount.divide(sum, 6, RoundingMode.HALF_UP).doubleValue() : 0));
                }
                if (accountGatherVO.getPayWay()==PayWay.POINT) {
                    accountGatherVO.setSumAmount("￥".concat(amountFormatter(tradeAccountRecordResponse.getPointsPrice())));
                    accountGatherVO.setPercentage(fmt.format(BigDecimal.ZERO.compareTo(sum) != 0 ? tradeAccountRecordResponse.getPointsPrice().divide(sum, 6, RoundingMode.HALF_UP).doubleValue() : 0));
                }
            }

            AccountGatherVO pointsUser= accountGatherVOList.stream().filter(accountGatherVO -> accountGatherVO.getPayWay()==PayWay.POINTS_USED).findFirst().get();
            pointsUser.setPercentage("");
            pointsUser.setSumAmount(tradeAccountRecordResponse.getPoints().toString());

        }

        return BaseResponse.success(accountGatherVOList);
    }


    /**
     * 自定义格式化金额
     *
     * @return 金额格式化String
     */
    public static String amountFormatter(BigDecimal amount, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(amount);
    }

    /**
     * 格式化金额，格式：#,###.00
     *
     * @return 金额格式化String
     */
    public static String amountFormatter(BigDecimal amount) {
        return amountFormatter(amount, _AMOUNT_PATTERN_STYLE_NORM);
    }


    /**
     * 收入明细
     *
     * @param request 请求参数结构
     * @return 分页后的收入明细记录
     */
    @ApiOperation(value = "收入明细")
    @RequestMapping(value = "/income/details", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<AccountDetailsVO>> incomeDetails(@RequestBody @Valid
                                                                                  AccountDetailsPageRequest request) {
        request.setAccountRecordType(AccountRecordType.INCOME);
        MicroServicePage<AccountDetailsVO> voPage= accountRecordQueryProvider.pageAccountDetails(request).getContext().getAccountDetailsVOPage();
        //设置积分数量和金额
        if (CollectionUtils.isNotEmpty(voPage.getContent())) {
            List<String> orderCodes=  voPage.getContent().stream().map(AccountDetailsVO::getOrderCode).collect(Collectors.toList());

            TradeGetByIdsResponse tradeGetByIdsResponse = tradeQueryProvider.getByIds(TradeGetByIdsRequest.builder().tid(orderCodes).build()).getContext();
            if (CollectionUtils.isNotEmpty(tradeGetByIdsResponse.getTradeVO())) {
                voPage.getContent().forEach(accountDetailsVO -> {
                    tradeGetByIdsResponse.getTradeVO().forEach(tradeVO -> {
                          if (Objects.equals(tradeVO.getId(),accountDetailsVO.getOrderCode()) && Objects.nonNull(tradeVO.getTradePrice().getPoints())) {

                              if (Objects.nonNull(tradeVO.getTradePrice().getPoints())) {
                                  accountDetailsVO.setPoints(tradeVO.getTradePrice().getPoints());
                              }

                              if (Objects.nonNull(tradeVO.getTradePrice().getPointsPrice())) {
                                  accountDetailsVO.setPointsPrice(tradeVO.getTradePrice().getPointsPrice());
                              }

                          }
                    });
                });
            }

        }
        return BaseResponse.success(voPage);
    }

    /**
     * 导出收入明细
     *
     * @return
     */
    @ApiOperation(value = "导出收入明细")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "加密", required = true)
    @RequestMapping(value = "/income/details/export/{encrypted}", method = RequestMethod.GET)
    public void exportIncomeDetails(@PathVariable String encrypted, HttpServletResponse response) {
        exportDetails(encrypted, response, AccountRecordType.INCOME);
    }


    /**
     * 退款列表
     *
     * @param request 请求参数结构
     * @return 分页后的列表
     */
    @ApiOperation(value = "退款列表")
    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<AccountRecordVO>> pageRefund(@RequestBody @Valid AccountRecordPageRequest
                                                                              request) {
        request.setSupplierId(commonUtil.getCompanyInfoId());
        request.setAccountRecordType(AccountRecordType.REFUND);

        MicroServicePage<AccountRecordVO> accountRecordVOS= accountRecordQueryProvider.pageAccountRecord(request).getContext().getAccountRecordVOPage();


        //根据开始时间和结束时间，查询店铺下面的积分总额和积分数量
        RefundOrderAccountRecordResponse recordResponse= refundOrderQueryProvider.getRefundOrderAccountRecord(RefundOrderAccountRecordRequest.builder().beginTime(request.getBeginTime()).endTime(request.getEndTime()).companyInfoId(commonUtil.getStoreId()).build()).getContext();


        if (CollectionUtils.isNotEmpty(accountRecordVOS.getContent()) && Objects.nonNull(recordResponse)) {
            AccountRecordVO accountRecordVO=  accountRecordVOS.getContent().get(0);
            Map<String, String> payItemAmountMap = accountRecordVO.getPayItemAmountMap();
            payItemAmountMap.put("POINTS_USED","￥".concat(amountFormatter(recordResponse.getPointsPrice())));
            accountRecordVO.setPoints(recordResponse.getPoints());
        }
        return BaseResponse.success(accountRecordVOS);
    }

    /**
     * 退款支付方式汇总
     *
     * @param request 请求参数结构
     * @return 各退款方式金额汇总记录
     */
    @ApiOperation(value = "退款支付方式汇总")
    @RequestMapping(value = "/refund/gross", method = RequestMethod.POST)
    public BaseResponse<List<AccountGatherVO>> refundSummarizing(@RequestBody @Valid AccountGatherListRequest request) {
        request.setSupplierId(commonUtil.getCompanyInfoId());
        request.setAccountRecordType(AccountRecordType.REFUND);

        List<AccountGatherVO> accountGatherVOList = accountRecordQueryProvider.listAccountGather(request).getContext().getAccountGatherVOList();

        RefundOrderAccountRecordResponse recordResponse= refundOrderQueryProvider.getRefundOrderAccountRecord(RefundOrderAccountRecordRequest.builder().beginTime(request.getBeginTime()).endTime(request.getEndTime()).companyInfoId(commonUtil.getStoreId()).build()).getContext();


        if (CollectionUtils.isNotEmpty(accountGatherVOList) && Objects.nonNull(recordResponse)) {

            BigDecimal sum=BigDecimal.ZERO;

            for(AccountGatherVO accountGatherVO:accountGatherVOList){
                String regEx="[\n`~￥？]";
                String newSumAmount = accountGatherVO.getSumAmount().replaceAll(regEx,"");
                BigDecimal sumAmount = new BigDecimal(newSumAmount);
                log.info("=======收入支付和sumAmount:{}======",sumAmount);
                if (accountGatherVO.getPayWay()!=PayWay.POINTS_USED) {
                    sum=sum.add(sumAmount);
                }
            }
            sum=sum.add(recordResponse.getPointsPrice());

            NumberFormat fmt = NumberFormat.getPercentInstance();
            //设置百分数精确度2即保留两位小数
            fmt.setMinimumFractionDigits(2);


            log.info("=======收入支付和:{}======",sum);

            for(AccountGatherVO accountGatherVO:accountGatherVOList){
                if (accountGatherVO.getPayWay()!=PayWay.POINTS_USED || accountGatherVO.getPayWay()!=PayWay.POINT) {
                    String regEx="[\n`~￥？]";
                    String newSumAmount = accountGatherVO.getSumAmount().replaceAll(regEx,"");
                    BigDecimal sumAmount = new BigDecimal(newSumAmount);
                    accountGatherVO.setPercentage(fmt.format(BigDecimal.ZERO.compareTo(sum) != 0 ? sumAmount.divide(sum, 6, RoundingMode.HALF_UP).doubleValue() : 0));
                }
                if (accountGatherVO.getPayWay()==PayWay.POINT) {
                    accountGatherVO.setSumAmount("￥".concat(amountFormatter(recordResponse.getPointsPrice())));
                    accountGatherVO.setPercentage(fmt.format(BigDecimal.ZERO.compareTo(sum) != 0 ? recordResponse.getPointsPrice().divide(sum, 6, RoundingMode.HALF_UP).doubleValue() : 0));
                }
            }

            AccountGatherVO pointsUser= accountGatherVOList.stream().filter(accountGatherVO -> accountGatherVO.getPayWay()==PayWay.POINTS_USED).findFirst().get();
            pointsUser.setPercentage("");
            pointsUser.setSumAmount(recordResponse.getPoints().toString());

        }
        return BaseResponse.success(accountGatherVOList);
    }

    /**
     * 退款明细
     *
     * @param request 请求参数结构
     * @return 分页后的退款明细记录
     */
    @ApiOperation(value = "退款明细")
    @RequestMapping(value = "/refund/details", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<AccountDetailsVO>> refundDetails(@RequestBody @Valid
                                                                                  AccountDetailsPageRequest request) {
        request.setAccountRecordType(AccountRecordType.REFUND);

        MicroServicePage<AccountDetailsVO> voPage=  accountRecordQueryProvider.pageAccountDetails(request).getContext().getAccountDetailsVOPage();
        //设置积分数量和金额
        if (CollectionUtils.isNotEmpty(voPage.getContent())) {
             voPage.getContent().forEach(accountDetailsVO -> {
                 if (Objects.nonNull(accountDetailsVO.getReturnOrderCode())) {
                     RefundOrderByReturnOrderCodeRequest refundOrderByReturnOrderCodeRequest=new RefundOrderByReturnOrderCodeRequest();
                     refundOrderByReturnOrderCodeRequest.setReturnOrderCode(accountDetailsVO.getReturnOrderCode());
                     RefundOrderByReturnCodeResponse returnCodeResponse= refundOrderQueryProvider.getByReturnOrderCode(refundOrderByReturnOrderCodeRequest).getContext();
                     if (Objects.nonNull(returnCodeResponse) && Objects.nonNull(returnCodeResponse.getReturnPoints())) {
                         log.info("===========对账退款明细id：{}，ReturnOrderCode:{},积分：{}=============",returnCodeResponse.getRefundId(),accountDetailsVO.getReturnOrderCode(),returnCodeResponse.getReturnPoints());
                         accountDetailsVO.setPoints(returnCodeResponse.getReturnPoints());
                         BigDecimal bigDecimal=new BigDecimal(returnCodeResponse.getReturnPoints());
                         accountDetailsVO.setPointsPrice(bigDecimal.divide(new BigDecimal(100)));
                     }
                 }
            });
        }
        return BaseResponse.success(voPage);
    }

    /**
     * 导出退款明细
     *
     * @return
     */
    @ApiOperation(value = "导出退款明细")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "加密", required = true)
    @RequestMapping(value = "/refund/details/export/{encrypted}", method = RequestMethod.GET)
    public void exportRefundDetails(@PathVariable String encrypted, HttpServletResponse response) {
        exportDetails(encrypted, response, AccountRecordType.REFUND);
    }

    /**
     * 获取所有支付方式
     *
     * @return 支付方式List key:枚举类型PayWay(支付方式)的name  value:枚举类型PayWay(支付方式)的中文描述
     */
    @ApiOperation(value = "获取所有支付方式",
            notes = "支付方式List key:枚举类型PayWay(支付方式)的name  value:枚举类型PayWay(支付方式)的中文描述")
    @RequestMapping(value = "/pay-methods", method = RequestMethod.GET)
    public BaseResponse<Map<String, String>> payWays() {
        return BaseResponse.success(Arrays.stream(PayWay.values()).collect(Collectors.toMap(PayWay::toValue, PayWay::getDesc)));
    }

    private void exportDetails(String encrypted, HttpServletResponse response, AccountRecordType type) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        AccountDetailsExportRequest request = JSON.parseObject(decrypted, AccountDetailsExportRequest.class);
        request.setAccountRecordType(type);
        String begin = DateUtil.format(DateUtil.parse(request.getBeginTime(), DateUtil.FMT_TIME_1), DateUtil.FMT_TIME_5);
        String end = DateUtil.format(DateUtil.parse(request.getEndTime(), DateUtil.FMT_TIME_1), DateUtil.FMT_TIME_5);

        List<AccountDetailsVO> details = accountRecordQueryProvider.exportAccountDetailsLoad(request).getContext().getAccountDetailsVOList();

//        Store store = storeService.find(request.getStoreId());
        StoreVO store = storeQueryProvider.getById(new StoreByIdRequest(request.getStoreId())).getContext().getStoreVO();
        boolean flag = type.toValue() == 0;
        String fileName = String.format("%s%s-%s%s对账明细.xls", store.getStoreName(), begin, end, flag ? "收入" : "退款");
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("/finance/income/export/, URLEncoding error,fileName={},", fileName, e);
        }
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName);
        response.setHeader(headerKey, headerValue);

        try {
            if (flag) {
                doExportIncomeDetails(details, response.getOutputStream());
            } else {
                doExportRefundDetails(details, response.getOutputStream());
            }
            response.flushBuffer();
        } catch (IOException e) {
            throw new SbcRuntimeException(e);
        }
    }

    /**
     * 财务对账导出
     * @param encrypted
     * @throws Exception
     */
    @ApiOperation(value = "财务对账导出")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "加密", required = true)
    @GetMapping(value = "/exportIncome/{encrypted}")
    public void exportIncome(@PathVariable String encrypted) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        AccountRecordToExcelRequest request = JSON.parseObject(decrypted, AccountRecordToExcelRequest.class);
        request.setSupplierId(commonUtil.getCompanyInfoId());

        String file=null;
        // 返回导出内容
        AccountRecordExcelVO excelData = accountRecordProvider.writeAccountRecordToExcel(request).getContext().getAccountRecordExcel();


        //表头汇总信息
        List<AccountGatherVO> accountGatherVOList = excelData.getAccountGathers();

        //根据开始时间和结束时间，查询店铺下面的积分总额和积分数量
        TradeAccountRecordResponse tradeAccountRecordResponse= tradeQueryProvider.getTradeAccountRecord(TradeAccountRecordRequest.builder().beginTime(request.getBeginTime()).endTime(request.getEndTime()).companyInfoId(commonUtil.getStoreId()).build()).getContext();

        if (CollectionUtils.isNotEmpty(accountGatherVOList) && Objects.nonNull(tradeAccountRecordResponse)) {

            BigDecimal sum=BigDecimal.ZERO;

            for(AccountGatherVO accountGatherVO:accountGatherVOList){
                String regEx="[\n`~￥？]";
                String newSumAmount = accountGatherVO.getSumAmount().replaceAll(regEx,"");
                BigDecimal sumAmount = new BigDecimal(newSumAmount);
                log.info("=======收入支付和sumAmount:{}======",sumAmount);
                if (accountGatherVO.getPayWay()!=PayWay.POINTS_USED) {
                    sum=sum.add(sumAmount);
                }
            }
            sum=sum.add(tradeAccountRecordResponse.getPointsPrice());

            NumberFormat fmt = NumberFormat.getPercentInstance();
            //设置百分数精确度2即保留两位小数
            fmt.setMinimumFractionDigits(2);


            log.info("=======收入支付和:{}======",sum);

            for(AccountGatherVO accountGatherVO:accountGatherVOList){
                if (accountGatherVO.getPayWay()!=PayWay.POINTS_USED || accountGatherVO.getPayWay()!=PayWay.POINT) {
                    String regEx="[\n`~￥？]";
                    String newSumAmount = accountGatherVO.getSumAmount().replaceAll(regEx,"");
                    BigDecimal sumAmount = new BigDecimal(newSumAmount);
                    accountGatherVO.setPercentage(fmt.format(BigDecimal.ZERO.compareTo(sum) != 0 ? sumAmount.divide(sum, 6, RoundingMode.HALF_UP).doubleValue() : 0));
                }
                if (accountGatherVO.getPayWay()==PayWay.POINT) {
                    accountGatherVO.setSumAmount("￥".concat(amountFormatter(tradeAccountRecordResponse.getPointsPrice())));
                    accountGatherVO.setPercentage(fmt.format(BigDecimal.ZERO.compareTo(sum) != 0 ? tradeAccountRecordResponse.getPointsPrice().divide(sum, 6, RoundingMode.HALF_UP).doubleValue() : 0));
                }
            }

            AccountGatherVO pointsUser= accountGatherVOList.stream().filter(accountGatherVO -> accountGatherVO.getPayWay()==PayWay.POINTS_USED).findFirst().get();
            pointsUser.setPercentage("");
            pointsUser.setSumAmount(tradeAccountRecordResponse.getPoints().toString());

        }

        //店铺汇总信息
        List<AccountRecordVO> accountRecords = excelData.getAccountRecords();

        if (CollectionUtils.isNotEmpty(accountRecords)) {
            AccountRecordVO accountRecordVO=  accountRecords.get(0);
            Map<String, String> payItemAmountMap = accountRecordVO.getPayItemAmountMap();
            payItemAmountMap.put("POINTS_USED",tradeAccountRecordResponse.getPoints().toString());
            payItemAmountMap.put("POINT","￥".concat(amountFormatter(tradeAccountRecordResponse.getPointsPrice())));
            accountRecordVO.setPoints(tradeAccountRecordResponse.getPoints());

            BigDecimal totalAmount=BigDecimal.ZERO;

            for(String key:payItemAmountMap.keySet()){
                if (!Objects.equals(key,PayWay.POINTS_USED.name())) {
                    log.info("=============key:{},value:{}===============",key, payItemAmountMap.get(key));
                    String regEx="[\n`~￥？]";
                    String newSumAmount = payItemAmountMap.get(key).replaceAll(regEx,"");
                    BigDecimal sumAmount = new BigDecimal(newSumAmount);
                    totalAmount= totalAmount.add(sumAmount);
                }
            }
            accountRecordVO.setTotalAmount(totalAmount.toString());

        }


        //导出退款对账
        List<AccountGatherVO> returnGathers = excelData.getReturnGathers();

        RefundOrderAccountRecordResponse recordResponse= refundOrderQueryProvider.getRefundOrderAccountRecord(RefundOrderAccountRecordRequest.builder().beginTime(request.getBeginTime()).endTime(request.getEndTime()).companyInfoId(commonUtil.getStoreId()).build()).getContext();


        if (CollectionUtils.isNotEmpty(returnGathers) && Objects.nonNull(recordResponse)) {

            BigDecimal sum=BigDecimal.ZERO;

            for(AccountGatherVO accountGatherVO:returnGathers){
                String regEx="[\n`~￥？]";
                String newSumAmount = accountGatherVO.getSumAmount().replaceAll(regEx,"");
                BigDecimal sumAmount = new BigDecimal(newSumAmount);
                log.info("=======收入支付和sumAmount:{}======",sumAmount);
                if (accountGatherVO.getPayWay()!=PayWay.POINTS_USED) {
                    sum=sum.add(sumAmount);
                }
            }
            sum=sum.add(recordResponse.getPointsPrice());

            NumberFormat fmt = NumberFormat.getPercentInstance();
            //设置百分数精确度2即保留两位小数
            fmt.setMinimumFractionDigits(2);


            log.info("=======收入支付和:{}======",sum);

            for(AccountGatherVO accountGatherVO:returnGathers){

                log.info("==========accountGatherVO:{}============",accountGatherVO);

                if (accountGatherVO.getPayWay()!=PayWay.POINTS_USED || accountGatherVO.getPayWay()!=PayWay.POINT) {
                    String regEx="[\n`~￥？]";
                    String newSumAmount = accountGatherVO.getSumAmount().replaceAll(regEx,"");
                    BigDecimal sumAmount = new BigDecimal(newSumAmount);
                    accountGatherVO.setPercentage(fmt.format(BigDecimal.ZERO.compareTo(sum) != 0 ? sumAmount.divide(sum, 6, RoundingMode.HALF_UP).doubleValue() : 0));
                }
                if (accountGatherVO.getPayWay()==PayWay.POINT) {
                    accountGatherVO.setSumAmount("￥".concat(amountFormatter(recordResponse.getPointsPrice())));
                    accountGatherVO.setPercentage(fmt.format(BigDecimal.ZERO.compareTo(sum) != 0 ? recordResponse.getPointsPrice().divide(sum, 6, RoundingMode.HALF_UP).doubleValue() : 0));
                }
            }
            AccountGatherVO pointsUser= returnGathers.stream().filter(accountGatherVO -> accountGatherVO.getPayWay()==PayWay.POINTS_USED).findFirst().get();
            pointsUser.setPercentage("");
            pointsUser.setSumAmount(recordResponse.getPoints().toString());

        }


        //退款店铺数据汇总
        List<AccountRecordVO> returnRecords=excelData.getReturnRecords();

        if (CollectionUtils.isNotEmpty(returnRecords)) {
            AccountRecordVO accountRecordVO=  returnRecords.get(0);
            Map<String, String> payItemAmountMap = accountRecordVO.getPayItemAmountMap();
            payItemAmountMap.put("POINTS_USED",recordResponse.getPoints().toString());
            payItemAmountMap.put("POINT","￥".concat(amountFormatter(recordResponse.getPointsPrice())));
            accountRecordVO.setPoints(recordResponse.getPoints());

            BigDecimal totalAmount=BigDecimal.ZERO;

            for(String key:payItemAmountMap.keySet()){
                if (!Objects.equals(key,PayWay.POINTS_USED.name())) {
                    log.info("=============key:{},value:{}===============",key, payItemAmountMap.get(key));
                    String regEx="[\n`~￥？]";
                    String newSumAmount = payItemAmountMap.get(key).replaceAll(regEx,"");
                    BigDecimal sumAmount = new BigDecimal(newSumAmount);
                    totalAmount= totalAmount.add(sumAmount);
                }
            }
            accountRecordVO.setTotalAmount(totalAmount.toString());
        }

        try (InputStream inputStream = templateFile.getInputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        ) {
            XSSFCellStyle xssfCellStyle = workbook.createCellStyle();
            xssfCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            writeExcel(workbook, excelData, xssfCellStyle);
            workbook.setActiveSheet(request.getAccountRecordType() == null ? 0 : request.getAccountRecordType().toValue());
            workbook.write(baos);
            file= Base64.getMimeEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            log.error("导出Excel错误:" + e);
            throw new SbcRuntimeException(CommonErrorCode.FAILED, e);
        }


        if (Objects.nonNull(excelData)) {
            try {
                String fileName = getFileName(request.getBeginTime(),request.getEndTime()) + "." + EXCEL_TYPE;
                fileName = URLEncoder.encode(fileName,"UTF-8");
                // 写入到response
                HttpUtil.getResponse().setContentType("application/vnd.ms-excel");
                HttpUtil.getResponse().setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";" +
                        "filename*=\"utf-8''%s\"", fileName, fileName));
                HttpUtil.getResponse().getOutputStream().write(new BASE64Decoder().decodeBuffer(file));
            } catch (Exception e) {
                throw new SbcRuntimeException(CommonErrorCode.FAILED);
            }
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
    private void writeExcel(XSSFWorkbook xssfWorkbook, AccountRecordExcelVO excelData, XSSFCellStyle xssfCellStyle) throws Exception {
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
    private void writeExcelTotal(XSSFSheet xssfSheet, List<AccountGatherVO> accountGathers, XSSFCellStyle xssfCellStyle, Byte type) {
        accountGathers = filterGather(accountGathers, type);


        log.info("===========退款店铺汇总：{},size:{}=============",accountGathers,accountGathers.size());

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
    private List<AccountGatherVO> filterGather(List<AccountGatherVO> accountGathers, Byte type) {
        return accountGathers.stream().filter(accountGather -> (!(StringUtils.equals(accountGather.getPayWay().toValue(),
                PayWay.ADVANCE.name()))) &&
                (!(StringUtils.equals(accountGather.getPayWay().toValue(), PayWay.COUPON.name())))).collect(Collectors.toList());
    }

    /**
     * 写入各店铺汇总数据
     *
     * @param xssfSheet
     * @param accountRecords
     * @param xssfCellStyle
     * @param type
     */
    private void writeExcelStore(XSSFSheet xssfSheet, List<AccountRecordVO> accountRecords, XSSFCellStyle xssfCellStyle, Byte type) {
        for (int rowNum = 0; rowNum < accountRecords.size(); rowNum++) {
            //从第7行开始写入
            XSSFRow xssfRow = getRow(xssfSheet, rowNum + 6);
            AccountRecordVO accountRecord = accountRecords.get(rowNum);
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
    private List<String> converRecord(AccountRecordVO accountRecord, Integer rowNum, Byte type) {

        log.info("===========店铺汇总数据：{}===============",accountRecord);

        log.info("===========店铺汇总数据Map：{}===============",accountRecord.getPayItemAmountMap().size());

        if (ObjectUtils.isEmpty(accountRecord)) {
            return Collections.emptyList();
        }
        List<String> recordList = new ArrayList<>();
        recordList.add(String.valueOf(rowNum + 1));
        recordList.add(accountRecord.getStoreName());
        recordList.add(accountRecord.getPayItemAmountMap().get(PayWay.CASH.name()));
        recordList.add(accountRecord.getPayItemAmountMap().get(PayWay.UNIONPAY.name()));
        recordList.add(accountRecord.getPayItemAmountMap().get(PayWay.POINT.name()));
        recordList.add(accountRecord.getPayItemAmountMap().get(PayWay.POINTS_USED.name()));
        recordList.add(accountRecord.getPayItemAmountMap().get(PayWay.ALIPAY.name()));
        recordList.add(accountRecord.getPayItemAmountMap().get(PayWay.WECHAT.name()));
        recordList.add(accountRecord.getPayItemAmountMap().get(PayWay.UNIONPAY_B2B.name()));
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



    /**
     * 导出收入明细
     *
     * @param details      收入明细列表
     * @param outputStream OutputStream
     */
    @SuppressWarnings("unchecked")
    private void doExportIncomeDetails(List<AccountDetailsVO> details, ServletOutputStream outputStream) {

        //设置积分数量和金额
        if (CollectionUtils.isNotEmpty(details)) {
            List<String> orderCodes = details.stream().map(AccountDetailsVO::getOrderCode).collect(Collectors.toList());
            TradeGetByIdsResponse tradeGetByIdsResponse = tradeQueryProvider.getByIds(TradeGetByIdsRequest.builder().tid(orderCodes).build()).getContext();
            if (CollectionUtils.isNotEmpty(tradeGetByIdsResponse.getTradeVO())) {
                details.forEach(accountDetailsVO -> {
                    tradeGetByIdsResponse.getTradeVO().forEach(tradeVO -> {
                        if (Objects.equals(tradeVO.getId(), accountDetailsVO.getOrderCode()) && Objects.nonNull(tradeVO.getTradePrice().getPoints())) {

                            if (Objects.nonNull(tradeVO.getTradePrice().getPoints())) {
                                accountDetailsVO.setPoints(tradeVO.getTradePrice().getPoints());
                            }

                            if (Objects.nonNull(tradeVO.getTradePrice().getPointsPrice())) {
                                accountDetailsVO.setPointsPrice(tradeVO.getTradePrice().getPointsPrice());
                            }

                        }
                    });
                });
            }
        }

        ExcelHelper helper = new ExcelHelper();
        helper.addSheet("对账单收入明细", new Column[]{
                new Column("下单时间", new SpelColumnRender<AccountDetailsVO>("orderTime")),
                new Column("订单编号", new SpelColumnRender<AccountDetailsVO>("orderCode")),
                new Column("交易流水号", new SpelColumnRender<AccountDetailsVO>("tradeNo")),
                new Column("客户昵称", new SpelColumnRender<AccountDetailsVO>("customerName")),
                new Column("积分数量", new SpelColumnRender<AccountDetailsVO>("points")),
                new Column("积分金额", new SpelColumnRender<AccountDetailsVO>("pointsPrice")),
                new Column("支付时间", new SpelColumnRender<AccountDetailsVO>("tradeTime")),
                new Column("支付渠道", (cell, object) -> {
                    AccountDetailsVO d = (AccountDetailsVO) object;
                    cell.setCellValue(d.getPayWay().getDesc());
                }),
                new Column("支付金额", new SpelColumnRender<AccountDetailsVO>("amount"))
        }, details);
        helper.write(outputStream);
    }

    /**
     * 导出退款明细
     *
     * @param details      退款明细列表
     * @param outputStream OutputStream
     */
    @SuppressWarnings("unchecked")
    private void doExportRefundDetails(List<AccountDetailsVO> details, ServletOutputStream outputStream) {

        //设置积分数量和金额
        if (CollectionUtils.isNotEmpty(details)) {
            details.forEach(accountDetailsVO -> {
                if (Objects.nonNull(accountDetailsVO.getReturnOrderCode())) {
                    RefundOrderByReturnOrderCodeRequest refundOrderByReturnOrderCodeRequest=new RefundOrderByReturnOrderCodeRequest();
                    refundOrderByReturnOrderCodeRequest.setReturnOrderCode(accountDetailsVO.getReturnOrderCode());
                    RefundOrderByReturnCodeResponse returnCodeResponse= refundOrderQueryProvider.getByReturnOrderCode(refundOrderByReturnOrderCodeRequest).getContext();
                    if (Objects.nonNull(returnCodeResponse) && Objects.nonNull(returnCodeResponse.getReturnPoints())) {
                        log.info("===========对账退款明细id：{}，ReturnOrderCode:{},积分：{}=============",returnCodeResponse.getRefundId(),accountDetailsVO.getReturnOrderCode(),returnCodeResponse.getReturnPoints());
                        accountDetailsVO.setPoints(returnCodeResponse.getReturnPoints());
                        BigDecimal bigDecimal=new BigDecimal(returnCodeResponse.getReturnPoints());
                        accountDetailsVO.setPointsPrice(bigDecimal.divide(new BigDecimal(100)));
                    }
                }
            });
        }

        ExcelHelper helper = new ExcelHelper();
        helper.addSheet("对账单退款明细", new Column[]{
                new Column("退单时间", new SpelColumnRender("orderTime")),
                new Column("退单编号", new SpelColumnRender<AccountDetailsVO>("returnOrderCode")),
                new Column("订单编号", new SpelColumnRender<AccountDetailsVO>("orderCode")),
                new Column("交易流水号", new SpelColumnRender<AccountDetailsVO>("tradeNo")),
                new Column("客户昵称", new SpelColumnRender<AccountDetailsVO>("customerName")),
                new Column("积分数量", new SpelColumnRender<AccountDetailsVO>("points")),
                new Column("积分金额", new SpelColumnRender<AccountDetailsVO>("pointsPrice")),
                new Column("退款时间", new SpelColumnRender<AccountDetailsVO>("tradeTime")),
                new Column("退款渠道", (cell, object) -> {
                    AccountDetailsVO d = (AccountDetailsVO) object;
                    cell.setCellValue(d.getPayWay().getDesc());
                }),
                new Column("退款金额", new SpelColumnRender<AccountDetailsVO>("amount"))
        }, details);
        helper.write(outputStream);
    }


    /**
     * 财务对账获取excel名字
     * @param beginTime
     * @param endTime
     * @return
     */
    private String getFileName(String beginTime,String endTime) {
        String theme = EXCEL_NAME + beginTime.substring(0,10) + "～" + endTime.substring
                (0,10);
        return theme;
    }
}
