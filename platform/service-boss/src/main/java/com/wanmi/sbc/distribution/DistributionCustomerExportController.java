package com.wanmi.sbc.distribution;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.SiteResultCode;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerExportRequest;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerVO;
import com.wanmi.sbc.elastic.api.provider.customer.EsDistributionCustomerQueryProvider;
import com.wanmi.sbc.elastic.api.request.customer.EsDistributionCustomerPageRequest;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 分销员导出接口
 * Created by of2975 on 2019/4/13.
 */
@Api(description = "分销员导出API", tags = "BossDistributionCustomerExportController")
@Slf4j
@RestController
@Validated
@RequestMapping(value = "/distribution/customer")
public class DistributionCustomerExportController {

    @Autowired
    private DistributionCustomerQueryProvider queryProvider;

    @Autowired
    private EsDistributionCustomerQueryProvider  esDistributionCustomerQueryProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    private AtomicInteger exportCount = new AtomicInteger(0);

    /**
     * 导出分销员
     * @param encrypted
     * @param response
     */
    @ApiOperation(value = "导出分销员")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "解密", required = true)
    @RequestMapping(value = "/export/params/{encrypted}", method = RequestMethod.GET)
    public void export(@PathVariable String encrypted, HttpServletResponse response) {
        try {
            if (exportCount.incrementAndGet() > 1) {
                throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
            }

            String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
            DistributionCustomerExportRequest queryReq = JSON.parseObject(decrypted, DistributionCustomerExportRequest.class);
            // 具备分销员资格
            queryReq.setDistributorFlag(DefaultFlag.YES);
            // 导出数据查询

            EsDistributionCustomerPageRequest pageRequest = EsDistributionCustomerPageRequest.builder().build();
            BeanUtils.copyProperties(queryReq,pageRequest);
            List<DistributionCustomerVO> dataRecords = esDistributionCustomerQueryProvider.export(pageRequest).getContext().getDistributionCustomerVOList();
            //List<DistributionCustomerVO> dataRecords = queryProvider.export(queryReq).getContext().getDistributionCustomerVOList();

            String headerKey = "Content-Disposition";
            LocalDateTime dateTime = LocalDateTime.now();
            String fileName = String.format("批量导出分销员_%s.xls", dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
            String fileNameNew = fileName;
            try {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("/distribution/customer/export/params, fileName={},", fileName, e);
            }
            String headerValue = String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName);
            response.setHeader(headerKey, headerValue);


            try {
                exportCustomer(dataRecords, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                throw new SbcRuntimeException(e);
            }

            operateLogMQUtil.convertAndSend("营销", "批量导出分销员", fileNameNew);

        } catch (Exception e) {
             log.error("/distribution/customer/export/params error: ", e);
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
    public void exportCustomer(List<DistributionCustomerVO> dataRecords, OutputStream outputStream){
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
                new Column("分销员名称", new SpelColumnRender<DistributionCustomerVO>("customerName")),
                new Column("分销员账号", new SpelColumnRender<DistributionCustomerVO>("customerAccount")),
                new Column("分销员等级", new SpelColumnRender<DistributionCustomerVO>("distributorLevelName")),
                new Column("加入时间", new SpelColumnRender<DistributionCustomerVO>("createTime")),
                new Column("邀新人数", new SpelColumnRender<DistributionCustomerVO>("inviteCount")),
                new Column("有效邀新数", new SpelColumnRender<DistributionCustomerVO>("inviteAvailableCount")),
                new Column("已入账邀新奖金", new SpelColumnRender<DistributionCustomerVO>("rewardCash")),
                new Column("未入账邀新奖金", new SpelColumnRender<DistributionCustomerVO>("rewardCashNotRecorded")),
                new Column("分销订单", new SpelColumnRender<DistributionCustomerVO>("distributionTradeCount")),
                new Column("销售额", new SpelColumnRender<DistributionCustomerVO>("sales")),
                new Column("已入账分销佣金", new SpelColumnRender<DistributionCustomerVO>("commission")),
                new Column("未入账分销佣金", new SpelColumnRender<DistributionCustomerVO>("commissionNotRecorded")),
                new Column("账号状态", (cell, object) -> {
                    DistributionCustomerVO distributionCustomer = (DistributionCustomerVO) object;
                    if (distributionCustomer.getForbiddenFlag().equals(DefaultFlag.YES)) {
                        cell.setCellValue("禁止分销");
                    } else if(distributionCustomer.getForbiddenFlag().equals(DefaultFlag.NO)) {
                        cell.setCellValue("启用");
                    }
                }),
        };

        excelHelper.addSheet(
                "分销员导出",
                columns,
                dataRecords
        );
        excelHelper.write(outputStream);

    }



}

