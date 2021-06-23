package com.wanmi.sbc.funds;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.account.api.provider.funds.CustomerFundsDetailQueryProvider;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsDetailExportRequest;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsDetailPageRequest;
import com.wanmi.sbc.account.bean.enums.FundsType;
import com.wanmi.sbc.account.bean.vo.CustomerFundsDetailVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.SiteResultCode;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.customer.bean.enums.TabType;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 余额明细bff
 * @author: Geek Wang
 * @createDate: 2019/2/20 15:29
 * @version: 1.0
 */
@RestController("bossCustomerFundsDetailController")
@RequestMapping("/funds-detail")
@Api(tags = "CustomerFundsDetailController", description = "S2B 平台端-余额明细API")
@Slf4j
public class CustomerFundsDetailController {

    @Autowired
    private CustomerFundsDetailQueryProvider customerFundsDetailQueryProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    private AtomicInteger exportCount = new AtomicInteger(0);


    /**
     * 获取余额明细分页列表
     * @param request
     * @return
     */
    @ApiOperation(value = "S2B 平台端-获取余额明细分页列表")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<CustomerFundsDetailVO>> page(@RequestBody CustomerFundsDetailPageRequest request) {
        return BaseResponse.success(customerFundsDetailQueryProvider.page(request).getContext().getMicroServicePage());
    }



    
    /**
     * 导出分销员佣金明细/会员资金明细
     * @param encrypted
     * @param response
     */
    @ApiOperation(value = "导出分销员佣金明细/会员资金明细")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "解密", required = true)
    @RequestMapping(value = "/export/params/{encrypted}", method = RequestMethod.GET)
    public void export(@PathVariable String encrypted, HttpServletResponse response) {
        try {
            if (exportCount.incrementAndGet() > 1) {
                throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
            }

            String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
            CustomerFundsDetailExportRequest queryReq = JSON.parseObject(decrypted, CustomerFundsDetailExportRequest.class);
            // 导出数据查询
            List<CustomerFundsDetailVO> dataRecords = customerFundsDetailQueryProvider.export(queryReq).getContext().getMicroServicePage();

            String headerKey = "Content-Disposition";
            LocalDateTime dateTime = LocalDateTime.now();
            String fileName;
            // 根据tabType 判断分销员佣金明细 or 会员资金明细
            if(queryReq.getTabType().equals(TabType.COMMISSION.toValue())){
                fileName = String.format("批量导出分销员佣金明细_%s.xls", dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
            }else{
                fileName = String.format("批量导出会员资金明细_%s.xls", dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
            }
            String fileNameNew = fileName;
            try {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("/funds-detail/export/params, fileName={},", fileName, e);
            }
            String headerValue = String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName);
            response.setHeader(headerKey, headerValue);


            try {
                exportDetail(dataRecords, response.getOutputStream(),queryReq);
                response.flushBuffer();
            } catch (IOException e) {
                throw new SbcRuntimeException(e);
            }

            if(queryReq.getTabType().equals(TabType.COMMISSION.toValue())) {
                operateLogMQUtil.convertAndSend("财务", "批量导出分销员佣金明细", fileNameNew);
            }else{
                operateLogMQUtil.convertAndSend("财务", "批量导出会员资金明细", fileNameNew);
            }

        } catch (Exception e) {
            log.error("/funds-detail/export/params error: ", e);
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
    public void exportDetail(List<CustomerFundsDetailVO> dataRecords, OutputStream outputStream,CustomerFundsDetailExportRequest queryReq){
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
                new Column("入账时间", new SpelColumnRender<CustomerFundsDetailVO>("createTime")),
                new Column("业务编号", new SpelColumnRender<CustomerFundsDetailVO>("businessId")),
                new Column("账务类型", (cell, object) -> {
                    CustomerFundsDetailVO customerFundsDetailVO = (CustomerFundsDetailVO) object;
                    if(Objects.nonNull(customerFundsDetailVO.getFundsType())) {
                        // 佣金提现 分销佣金 邀新奖励
                        cell.setCellValue(customerFundsDetailVO.getFundsType().getDesc());
                    }
                }),
                new Column("收支金额", (cell, object) -> {
                    CustomerFundsDetailVO customerFundsDetailVO = (CustomerFundsDetailVO) object;
                    if(Objects.nonNull(customerFundsDetailVO.getReceiptPaymentAmount())) {
                        if(Objects.nonNull(customerFundsDetailVO.getFundsType())) {
                            if (customerFundsDetailVO.getFundsType().equals(FundsType.COMMISSION_WITHDRAWAL) || customerFundsDetailVO.getFundsType().equals(FundsType.BALANCE_PAY)) {
                                // 支出
                                cell.setCellValue("-"+customerFundsDetailVO.getReceiptPaymentAmount());
                            } else {
                                // 收入
                                cell.setCellValue("+"+customerFundsDetailVO.getReceiptPaymentAmount());
                            }
                        }
                    } else {
                        cell.setCellValue("0");
                    }
                }),
        };
        Column[] customerColumn = {
                new Column("账户余额", (cell, object) -> {
                    CustomerFundsDetailVO customerFundsDetailVO = (CustomerFundsDetailVO) object;
                    if (Objects.nonNull(customerFundsDetailVO.getAccountBalance())) {
                        cell.setCellValue(customerFundsDetailVO.getAccountBalance().toString());
                    } else {
                        cell.setCellValue("0");
                    }
                }),
        };

        String sheetName="明细导出";
        Column[] newColumns;
        if(queryReq.getTabType().equals(TabType.COMMISSION.toValue())) {
            if(Objects.nonNull(queryReq.getCustomerFundsDetailIdList())) {
                sheetName="分销员佣金明细导出";
            }else {
                sheetName = getFundsTypeString(queryReq, sheetName);
            }
            newColumns = columns;
        }else{
            if(Objects.nonNull(queryReq.getCustomerFundsDetailIdList())) {
                sheetName="会员资金余额明细导出";
            }else {
                sheetName = getTabTypeString(queryReq, sheetName);
            }
            newColumns = ArrayUtils.addAll(columns, customerColumn);
        }
        excelHelper.addSheet(
                sheetName,
                newColumns,
                dataRecords
        );
        excelHelper.write(outputStream);

    }

    /**
     * 获取tab类型名称
     *
     * @param queryReq
     * @param sheetName
     * @return
     */
    private String getTabTypeString(CustomerFundsDetailExportRequest queryReq, String sheetName) {
        if(Objects.nonNull(queryReq.getTabType())) {
            //全部 收入 支出
            sheetName = TabType.fromValue(queryReq.getTabType()).getDesc();
        }
        return sheetName;
    }

    /**
     * 获取账户类型名称
     * @param queryReq
     * @param sheetName
     * @return
     */
    private String getFundsTypeString(CustomerFundsDetailExportRequest queryReq, String sheetName) {
        if(Objects.nonNull(queryReq.getFundsType())) {
            //全部 分销佣金 邀新奖励
            sheetName = queryReq.getFundsType().getDesc();
        }
        return sheetName;
    }


}
