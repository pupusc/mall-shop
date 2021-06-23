package com.wanmi.sbc.finance;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.account.api.provider.finance.record.AccountRecordProvider;
import com.wanmi.sbc.account.api.provider.finance.record.AccountRecordQueryProvider;
import com.wanmi.sbc.account.api.request.finance.record.*;
import com.wanmi.sbc.account.bean.enums.AccountRecordType;
import com.wanmi.sbc.account.bean.enums.PayWay;
import com.wanmi.sbc.account.bean.vo.AccountDetailsVO;
import com.wanmi.sbc.account.bean.vo.AccountGatherVO;
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
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
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

    @Resource
    private CommonUtil commonUtil;

    public static final String EXCEL_NAME = "财务对账";

    public static final String EXCEL_TYPE = "xlsx";

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
        return BaseResponse.success(accountRecordQueryProvider.pageAccountRecord(request).getContext()
                .getAccountRecordVOPage());
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
        return BaseResponse.success(accountRecordQueryProvider.listAccountGather(request).getContext()
                .getAccountGatherVOList());
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
        return BaseResponse.success(accountRecordQueryProvider.pageAccountDetails(request).getContext()
                .getAccountDetailsVOPage());
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
        return BaseResponse.success(accountRecordQueryProvider.pageAccountRecord(request).getContext()
                .getAccountRecordVOPage());
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
        return BaseResponse.success(accountRecordQueryProvider.listAccountGather(request).getContext()
                .getAccountGatherVOList());
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
        return BaseResponse.success(accountRecordQueryProvider.pageAccountDetails(request).getContext()
                .getAccountDetailsVOPage());
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
        // 返回导出内容
        String file = accountRecordProvider.writeAccountRecordToExcel(request).getContext().getFile();
        if (StringUtils.isNotBlank(file)) {
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
     * 导出收入明细
     *
     * @param details      收入明细列表
     * @param outputStream OutputStream
     */
    @SuppressWarnings("unchecked")
    private void doExportIncomeDetails(List<AccountDetailsVO> details, ServletOutputStream outputStream) {
        ExcelHelper helper = new ExcelHelper();
        helper.addSheet("对账单收入明细", new Column[]{
                new Column("下单时间", new SpelColumnRender<AccountDetailsVO>("orderTime")),
                new Column("订单编号", new SpelColumnRender<AccountDetailsVO>("orderCode")),
                new Column("交易流水号", new SpelColumnRender<AccountDetailsVO>("tradeNo")),
                new Column("客户昵称", new SpelColumnRender<AccountDetailsVO>("customerName")),
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
        ExcelHelper helper = new ExcelHelper();
        helper.addSheet("对账单退款明细", new Column[]{
                new Column("退单时间", new SpelColumnRender("orderTime")),
                new Column("退单编号", new SpelColumnRender<AccountDetailsVO>("returnOrderCode")),
                new Column("订单编号", new SpelColumnRender<AccountDetailsVO>("orderCode")),
                new Column("交易流水号", new SpelColumnRender<AccountDetailsVO>("tradeNo")),
                new Column("客户昵称", new SpelColumnRender<AccountDetailsVO>("customerName")),
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
