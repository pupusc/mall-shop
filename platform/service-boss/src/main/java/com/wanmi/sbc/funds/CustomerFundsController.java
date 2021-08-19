package com.wanmi.sbc.funds;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.account.api.constant.AccountRedisKey;
import com.wanmi.sbc.account.api.provider.funds.CustomerFundsQueryProvider;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsByCustomerIdRequest;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsExportRequest;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsPageRequest;
import com.wanmi.sbc.account.api.response.funds.CustomerFundsByCustomerIdResponse;
import com.wanmi.sbc.account.api.response.funds.CustomerFundsStatisticsResponse;
import com.wanmi.sbc.account.bean.vo.CustomerFundsVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.SensitiveUtils;
import com.wanmi.sbc.common.util.SiteResultCode;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.elastic.api.provider.customerFunds.EsCustomerFundsQueryProvider;
import com.wanmi.sbc.elastic.api.request.customerFunds.EsCustomerFundsPageRequest;
import com.wanmi.sbc.elastic.bean.vo.customerFunds.EsCustomerFundsVO;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 会员资金bff
 * @author: Geek Wang
 * @createDate: 2019/2/20 15:29
 * @version: 1.0
 */
@RestController("bossCustomerFundsController")
@RequestMapping("/funds")
@Api(tags = "CustomerFundsController", description = "S2B 平台端-会员资金API")
@Slf4j
public class CustomerFundsController {

    @Autowired
    private CustomerFundsQueryProvider customerFundsQueryProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private EsCustomerFundsQueryProvider esCustomerFundsQueryProvider;

    private AtomicInteger exportCount = new AtomicInteger(0);

    /**
     * 获取会员资金统计（会员余额总额、冻结余额总额、可提现余额总额）
     * @return
     */
    @ApiOperation(value = "S2B 平台端-获取会员资金统计（会员余额总额、冻结余额总额、可提现余额总额）")
    @RequestMapping(value = "/statistics", method = RequestMethod.POST)
    public BaseResponse<CustomerFundsStatisticsResponse> statistics() {
        String accountBalanceTotal = Objects.toString(redisService.getValueByKey(AccountRedisKey.ACCOUNT_BALANCE_TOTAL),"0.00");
        String blockedBalanceTotal = Objects.toString(redisService.getValueByKey(AccountRedisKey.BLOCKED_BALANCE_TOTAL),"0.00");
        String withdrawAmountTotal = Objects.toString(redisService.getValueByKey(AccountRedisKey.WITHDRAW_AMOUNT_TOTAL),"0.00");
        return BaseResponse.success(new CustomerFundsStatisticsResponse(BigDecimal.valueOf(Double.valueOf(accountBalanceTotal)),BigDecimal.valueOf(Double.valueOf(blockedBalanceTotal)),BigDecimal.valueOf(Double.valueOf(withdrawAmountTotal))));
    }

    /**
     * 根据会员ID查询会员资金信息
     * @param customerId
     * @return
     */
    @ApiOperation(value = "S2B 平台端-根据会员ID查询会员资金信息")
    @RequestMapping(value = "/statistics/{customerId}", method = RequestMethod.POST)
    public BaseResponse<CustomerFundsByCustomerIdResponse> getById(@PathVariable String customerId) {
        return BaseResponse.success(customerFundsQueryProvider.getByCustomerId(new CustomerFundsByCustomerIdRequest(customerId)).getContext());
    }

    /**
     * 获取会员资金分页列表
     * @param request
     * @return
     */
    @ApiOperation(value = "S2B 平台端-获取会员资金分页列表")
    @RequestMapping(value = "/oldPage", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<CustomerFundsVO>> page(@RequestBody CustomerFundsPageRequest request) {
        return BaseResponse.success(customerFundsQueryProvider.page(request).getContext().getMicroServicePage());
    }

    /**
     * 获取会员资金分页列表
     * @param request
     * @return
     */
    @ApiOperation(value = "S2B 平台端-获取会员资金分页列表")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<EsCustomerFundsVO>> esPage(@RequestBody EsCustomerFundsPageRequest request) {
        return BaseResponse.success( esCustomerFundsQueryProvider.esCustomerFundsPage(request).getContext().getEsCustomerFundsVOPage());
    }


    /**
     * 导出会员资金记录
     * @param encrypted
     * @param response
     */
    @ApiOperation(value = "导出会员资金记录")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "解密", required = true)
    @RequestMapping(value = "/export/params/{encrypted}", method = RequestMethod.GET)
    public void export(@PathVariable String encrypted, HttpServletResponse response) {
        try {
            if (exportCount.incrementAndGet() > 1) {
                throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
            }

            String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
            CustomerFundsExportRequest queryReq = JSON.parseObject(decrypted, CustomerFundsExportRequest.class);

            // 导出数据查询
            List<CustomerFundsVO> dataRecords = customerFundsQueryProvider.export(queryReq).getContext().getMicroServiceList();

            String headerKey = "Content-Disposition";
            LocalDateTime dateTime = LocalDateTime.now();
            String fileName = String.format("批量导出会员资金记录_%s.xls", dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
            String fileNameNew = fileName;
            try {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("/funds/export/params, fileName={},", fileName, e);
            }
            String headerValue = String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName);
            response.setHeader(headerKey, headerValue);


            try {
                exportFunds(dataRecords, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                throw new SbcRuntimeException(e);
            }

            operateLogMQUtil.convertAndSend("财务", "批量导出会员资金", fileNameNew);

        } catch (Exception e) {
            log.error("/funds/export/params error: ", e);
            throw new SbcRuntimeException(SiteResultCode.ERROR_000001);
        } finally {
            exportCount.set(0);
        }

    }
    /**
     * 导出excel
     * @param dataRecords
     * @param outputStream
     */
    public void exportFunds(List<CustomerFundsVO> dataRecords, OutputStream outputStream){
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
                new Column("会员名称", new SpelColumnRender<CustomerFundsVO>("customerName")),
                new Column("会员账号", (cell, object) -> {
                    CustomerFundsVO customerFundsVO = (CustomerFundsVO) object;
                    // 账号脱敏
                    cell.setCellValue(SensitiveUtils.handlerMobilePhone(customerFundsVO.getCustomerAccount()));
                }),
                new Column("会员等级", (cell, object) -> {
                    CustomerFundsVO customerFundsVO = (CustomerFundsVO) object;
                    if (Objects.nonNull(customerFundsVO.getDistributor()) && customerFundsVO.getDistributor() ==1
                            && StringUtils.isNotBlank(customerFundsVO.getCustomerLevelName())) {
                        cell.setCellValue("分销员 & " + customerFundsVO.getCustomerLevelName());
                    }else {
                        if (Objects.nonNull(customerFundsVO.getDistributor()) && customerFundsVO.getDistributor() == 1) {
                            cell.setCellValue("分销员");
                        }
                        if (StringUtils.isNotBlank(customerFundsVO.getCustomerLevelName())) {
                            cell.setCellValue(customerFundsVO.getCustomerLevelName());
                        }
                    }
                }),
                new Column("账户余额", (cell, object) -> {
                    CustomerFundsVO customerFundsVO = (CustomerFundsVO) object;
                    if (Objects.nonNull(customerFundsVO.getAccountBalance())) {
                        cell.setCellValue(customerFundsVO.getAccountBalance().toString());
                    } else {
                        cell.setCellValue("0");
                    }
                }),
                new Column("冻结余额", (cell, object) -> {
                    CustomerFundsVO customerFundsVO = (CustomerFundsVO) object;
                    if (Objects.nonNull(customerFundsVO.getBlockedBalance())) {
                        cell.setCellValue(customerFundsVO.getBlockedBalance().toString());
                    } else {
                        cell.setCellValue("0");
                    }
                }),
                new Column("可提现余额", (cell, object) -> {
                    CustomerFundsVO customerFundsVO = (CustomerFundsVO) object;
                    if (Objects.nonNull(customerFundsVO.getWithdrawAmount())) {
                        cell.setCellValue(customerFundsVO.getWithdrawAmount().toString());
                    } else {
                        cell.setCellValue("0");
                    }
                }),
        };

        excelHelper.addSheet(
                "会员资金导出",
                columns,
                dataRecords
        );
        excelHelper.write(outputStream);

    }


}
