package com.wanmi.sbc.distribution;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.SensitiveUtils;
import com.wanmi.sbc.common.util.SiteResultCode;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.customer.api.constant.DistributionCommissionRedisKey;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCommissionQueryProvider;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCommissionExportRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCommissionPageRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCommissionPageResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCommissionStatisticsResponse;
import com.wanmi.sbc.customer.bean.vo.DistributionCommissionForPageVO;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
 * Created by feitingting on 2019/2/26.
 */
@Api(tags = "DistributionCommissionController", description = "分销员佣金接口")
@Slf4j
@RestController
@RequestMapping("/distribution-commission")
public class DistributionCommissionController {
    @Autowired
    private  DistributionCommissionQueryProvider distributionCommissionQueryProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    private AtomicInteger exportCount = new AtomicInteger(0);

    @ApiOperation(value = "S2B 平台端-分页获取分销员佣金记录")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<DistributionCommissionPageResponse> findDistributionInviteNewRecord(@RequestBody DistributionCommissionPageRequest distributionCommissionPageRequest) {
        //默认按创建时间降序
        return distributionCommissionQueryProvider.findDistributionCommissionPage(distributionCommissionPageRequest);
    }

    @ApiOperation(value = "S2B 平台端-分销员佣金统计（佣金、分销佣金、邀新奖金、未入账分销佣金、未入账邀新奖金）")
    @RequestMapping(value = "/statistics", method = RequestMethod.POST)
    public BaseResponse<DistributionCommissionStatisticsResponse> statistics() {
        //佣金总额
        String commissionTotal = Objects.toString(redisService.getValueByKey(DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION_TOTAL),"0.00");
        //分销佣金
        String commission = Objects.toString(redisService.getValueByKey(DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION),"0.00");
        //邀新奖金
        String rewardCash = Objects.toString(redisService.getValueByKey(DistributionCommissionRedisKey.DISTRIBUTION_REWARD_CASH),"0.00");
        //未入账分销佣金
        String commissionNotRecorded = Objects.toString(redisService.getValueByKey(DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION_NOTRECORDED),"0.00");
        //未入账邀新奖金
        String rewardCashNotRecorded = Objects.toString(redisService.getValueByKey(DistributionCommissionRedisKey.DISTRIBUTION_REWARD_CASH_NOTRECORDED),"0.00");

        return BaseResponse.success(new DistributionCommissionStatisticsResponse(BigDecimal.valueOf(Double.valueOf(commissionTotal)),
                BigDecimal.valueOf(Double.valueOf(commission)),BigDecimal.valueOf(Double.valueOf(rewardCash)),
                BigDecimal.valueOf(Double.valueOf(commissionNotRecorded)),BigDecimal.valueOf(Double.valueOf(rewardCashNotRecorded))));
    }

    /**
     * 导出分销员佣金记录
     * @param encrypted
     * @param response
     */
    @ApiOperation(value = "导出分销员佣金记录")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "解密", required = true)
    @RequestMapping(value = "/export/params/{encrypted}", method = RequestMethod.GET)
    public void export(@PathVariable String encrypted, HttpServletResponse response) {
        try {
            if (exportCount.incrementAndGet() > 1) {
                throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
            }

            String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
            DistributionCommissionExportRequest queryReq = JSON.parseObject(decrypted, DistributionCommissionExportRequest.class);
            // 导出数据查询
            List<DistributionCommissionForPageVO> dataRecords = distributionCommissionQueryProvider.findDistributionCommissionExport(queryReq).getContext().getRecordList();

            String headerKey = "Content-Disposition";
            LocalDateTime dateTime = LocalDateTime.now();
            String fileName = String.format("批量导出分销员佣金记录_%s.xls", dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
            String fileNameNew = fileName;
            try {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("/distribution-commission/export/params, fileName={},", fileName, e);
            }
            String headerValue = String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName);
            response.setHeader(headerKey, headerValue);


            try {
                export(dataRecords, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                throw new SbcRuntimeException(e);
            }

            operateLogMQUtil.convertAndSend("财务", "批量导出分销员佣金", fileNameNew);

        } catch (Exception e) {
            log.error("/distribution-commission/export/params error: ", e);
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
    public void export(List<DistributionCommissionForPageVO> dataRecords, OutputStream outputStream){
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
                new Column("分销员名称", new SpelColumnRender<DistributionCommissionForPageVO>("customerName")),
                new Column("分销员账号", (cell, object) -> {
                    DistributionCommissionForPageVO distributionCommissionForPageVO = (DistributionCommissionForPageVO) object;
                    // 账号脱敏
                    cell.setCellValue(SensitiveUtils.handlerMobilePhone(distributionCommissionForPageVO.getCustomerAccount()));
                }),
                new Column("分销员等级", new SpelColumnRender<DistributionCommissionForPageVO>("distributorLevelName")),
                new Column("加入时间", new SpelColumnRender<DistributionCommissionForPageVO>("createTime")),
                new Column("账号状态", (cell, object) -> {
                    DistributionCommissionForPageVO distributionCommissionForPageVO = (DistributionCommissionForPageVO) object;
                    if (distributionCommissionForPageVO.getForbiddenFlag().equals(DefaultFlag.YES)) {
                        cell.setCellValue("禁止分销");
                    } else if(distributionCommissionForPageVO.getForbiddenFlag().equals(DefaultFlag.NO)) {
                        cell.setCellValue("启用");
                    }
                }),
                new Column("已入账佣金", (cell, object) -> {
                    DistributionCommissionForPageVO distributionCommissionForPageVO = (DistributionCommissionForPageVO) object;
                    if(Objects.nonNull(distributionCommissionForPageVO.getCommissionTotal())) {
                        cell.setCellValue(distributionCommissionForPageVO.getCommissionTotal().toString());
                    }else{
                        cell.setCellValue("0");
                    }
                }),
                new Column("已入账分销佣金", (cell, object) -> {
                    DistributionCommissionForPageVO distributionCommissionForPageVO = (DistributionCommissionForPageVO) object;
                    if(Objects.nonNull(distributionCommissionForPageVO.getCommission())) {
                        cell.setCellValue(distributionCommissionForPageVO.getCommission().toString());
                    }else{
                        cell.setCellValue("0");
                    }
                }),
                new Column("已入账邀新奖金", (cell, object) -> {
                    DistributionCommissionForPageVO distributionCommissionForPageVO = (DistributionCommissionForPageVO) object;
                    if(Objects.nonNull(distributionCommissionForPageVO.getRewardCash())) {
                        cell.setCellValue(distributionCommissionForPageVO.getRewardCash().toString());
                    }else{
                        cell.setCellValue("0");
                    }
                }),
                new Column("未入账分销佣金", (cell, object) -> {
                    DistributionCommissionForPageVO distributionCommissionForPageVO = (DistributionCommissionForPageVO) object;
                    if(Objects.nonNull(distributionCommissionForPageVO.getCommissionNotRecorded())) {
                        cell.setCellValue(distributionCommissionForPageVO.getCommissionNotRecorded().toString());
                    }else{
                        cell.setCellValue("0");
                    }
                }),
                new Column("未入账邀新奖金", (cell, object) -> {
                    DistributionCommissionForPageVO distributionCommissionForPageVO = (DistributionCommissionForPageVO) object;
                    if(Objects.nonNull(distributionCommissionForPageVO.getRewardCashNotRecorded())) {
                        cell.setCellValue(distributionCommissionForPageVO.getRewardCashNotRecorded().toString());
                    }else{
                        cell.setCellValue("0");
                    }
                }),
        };

        excelHelper.addSheet(
                "分销员佣金导出",
                columns,
                dataRecords
        );
        excelHelper.write(outputStream);

    }
}
