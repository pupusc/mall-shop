package com.wanmi.sbc.distribution;
import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.SensitiveUtils;
import com.wanmi.sbc.common.util.SiteResultCode;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.customer.api.request.customer.CustomerDetailPageRequest;
import com.wanmi.sbc.customer.api.request.customer.DistributionInviteNewExportRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerPageRequest;
import com.wanmi.sbc.customer.api.request.customer.DistributionInviteNewPageRequest;

import com.wanmi.sbc.customer.api.provider.distribution.DistributionInviteNewQueryProvider;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.response.customer.CustomerDetailPageResponse;
import com.wanmi.sbc.customer.api.response.customer.DistributionInviteNewPageResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerPageResponse;
import com.wanmi.sbc.customer.bean.enums.RewardFlag;
import com.wanmi.sbc.customer.bean.enums.RewardRecordedFlag;
import com.wanmi.sbc.customer.bean.vo.DistributionInviteNewForPageVO;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 分销员API
 */
@Api(tags = "DistributionInviteNewController", description = "邀新记录接口")
@Slf4j
@RestController
@RequestMapping("/distribution-invite-new")
public class DistributionInviteNewController {

    @Autowired
    private  DistributionInviteNewQueryProvider distributionInviteNewQueryProvider;

    @Autowired
    private  CustomerQueryProvider customerQueryProvider;

    @Autowired
    private DistributionCustomerQueryProvider distributionCustomerQueryProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    private AtomicInteger exportCount = new AtomicInteger(0);

    /**
     * 分页查询邀请记录
     * @param distributionInviteNewPageRequest
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<DistributionInviteNewPageResponse> findDistributionInviteNewRecord(@RequestBody DistributionInviteNewPageRequest distributionInviteNewPageRequest) {
        distributionInviteNewPageRequest.putSort("registerTime", SortType.DESC.toValue());
        return distributionInviteNewQueryProvider.findDistributionInviteNewRecord(distributionInviteNewPageRequest);
    }

    /**
     * 根据受邀人账号或名称联想查询会员信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/newInvited/list", method = RequestMethod.POST)
    public BaseResponse<CustomerDetailPageResponse> inviteNewList(@RequestBody CustomerDetailPageRequest request) {
        return customerQueryProvider.page(request);
    }

    /**
     * 根据分销员账号或名称联想会员信息
     */
    @RequestMapping(value = "/distributionCustomer/list", method = RequestMethod.POST)
    public BaseResponse<DistributionCustomerPageResponse> distributionCustomer(@RequestBody DistributionCustomerPageRequest request) {
        return distributionCustomerQueryProvider.page(request);
    }


    /**
     * 导出邀新记录
     * @param encrypted
     * @param response
     */
    @ApiOperation(value = "导出邀新记录")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "解密", required = true)
    @RequestMapping(value = "/export/params/{encrypted}", method = RequestMethod.GET)
    public void export(@PathVariable String encrypted, HttpServletResponse response) {
        try {
            if (exportCount.incrementAndGet() > 1) {
                throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
            }

            String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
            DistributionInviteNewExportRequest queryReq = JSON.parseObject(decrypted, DistributionInviteNewExportRequest.class);

            // 按注册时间降序
            queryReq.setSortColumn("registerTime");
            queryReq.setSortType("DESC");
            // 导出数据查询
            List<DistributionInviteNewForPageVO> dataRecords = distributionInviteNewQueryProvider.exportDistributionInviteNewRecord(queryReq).getContext().getRecordList();

            String headerKey = "Content-Disposition";
            LocalDateTime dateTime = LocalDateTime.now();
            String fileName = String.format("批量导出邀新记录_%s.xls", dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
            String fileNameNew = fileName;
            try {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("/distribution-invite-new/export/params, fileName={},", fileName, e);
            }
            String headerValue = String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName);
            response.setHeader(headerKey, headerValue);


            try {
                exportRecord(dataRecords, response.getOutputStream(), queryReq);
                response.flushBuffer();
            } catch (IOException e) {
                throw new SbcRuntimeException(e);
            }

            operateLogMQUtil.convertAndSend("营销", "批量导出邀新记录", fileNameNew);

        } catch (Exception e) {
            log.error("/distribution-invite-new/export/params error: ", e);
            throw new SbcRuntimeException(SiteResultCode.ERROR_000001);
        } finally {
            exportCount.set(0);
        }

    }

    /**
     * 导出excel
     * @param dataRecords
     * @param outputStream
     * @param queryReq
     */
    public void exportRecord(List<DistributionInviteNewForPageVO> dataRecords, OutputStream outputStream,DistributionInviteNewExportRequest queryReq){
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
                new Column("受邀人名称", new SpelColumnRender<DistributionInviteNewForPageVO>("invitedNewCustomerName")),
                new Column("受邀人账号", (cell, object) -> {
                    DistributionInviteNewForPageVO distributionInviteNewForPageVO = (DistributionInviteNewForPageVO) object;
                    // 账号脱敏
                    cell.setCellValue(SensitiveUtils.handlerMobilePhone(distributionInviteNewForPageVO.getInvitedNewCustomerAccount()));
                }),
                new Column("邀请人名称", new SpelColumnRender<DistributionInviteNewForPageVO>("requestCustomerName")),
                new Column("邀请人账号", (cell, object) -> {
                    DistributionInviteNewForPageVO distributionInviteNewForPageVO = (DistributionInviteNewForPageVO) object;
                    // 账号脱敏
                    cell.setCellValue(SensitiveUtils.handlerMobilePhone(distributionInviteNewForPageVO.getRequestCustomerAccount()));
                }),
                new Column("有效邀新", (cell, object) -> {
                    DistributionInviteNewForPageVO distributionInviteNew = (DistributionInviteNewForPageVO) object;
                    cell.setCellValue(distributionInviteNew.getAvailableDistribution().getDesc());
                }),
                new Column("注册时间", new SpelColumnRender<DistributionInviteNewForPageVO>("registerTime")),
                new Column("下单时间", new SpelColumnRender<DistributionInviteNewForPageVO>("firstOrderTime")),
                new Column("订单编号", new SpelColumnRender<DistributionInviteNewForPageVO>("orderCode")),
                new Column("订单完成时间", new SpelColumnRender<DistributionInviteNewForPageVO>("orderFinishTime")),
                new Column("奖励入账时间", new SpelColumnRender<DistributionInviteNewForPageVO>("rewardRecordedTime")),
        };

        Column[] isRewardRecordedColumns = {
                //奖励区分 奖励金额 优惠券
                new Column("奖励", (cell, object) -> {
                    DistributionInviteNewForPageVO distributionInviteNew = (DistributionInviteNewForPageVO) object;
                    if(Objects.nonNull(distributionInviteNew.getRewardFlag())){
                        if (distributionInviteNew.getRewardFlag().equals(RewardFlag.CASH)) {
                            // 现金
                            cell.setCellValue(distributionInviteNew.getRewardCash().toString());
                        } else if(distributionInviteNew.getRewardFlag().equals(RewardFlag.COUPON)) {
                            //优惠券
                            cell.setCellValue(distributionInviteNew.getRewardCoupon());
                        }
                    }
                }),
        };

        Column[] isNotRewardRecordedColumns = {
                //未入账奖励区分 奖励金额 优惠券
                new Column("未入账奖励", (cell, object) -> {
                    DistributionInviteNewForPageVO distributionInviteNew = (DistributionInviteNewForPageVO) object;
                    if(Objects.nonNull(distributionInviteNew.getRewardFlag())){
                        if (distributionInviteNew.getRewardFlag().equals(RewardFlag.CASH)) {
                            // 现金
                            cell.setCellValue(distributionInviteNew.getSettingAmount().toString());
                        } else if(distributionInviteNew.getRewardFlag().equals(RewardFlag.COUPON)) {
                            //优惠券
                            cell.setCellValue(distributionInviteNew.getSettingCoupons());
                        }
                    }
                }),
                new Column("未入账原因", (cell, object) -> {
                    DistributionInviteNewForPageVO distributionInviteNew = (DistributionInviteNewForPageVO) object;
                    if(Objects.nonNull(distributionInviteNew.getFailReasonFlag())) {
                        // 0:非有效邀新 1：奖励达到上限 2：奖励未开启
                        cell.setCellValue(distributionInviteNew.getFailReasonFlag().getDesc());
                    }
                }),
        };
        Column[] newColumns={};
        // sheetName 已入账 未入账
        String sheetName=queryReq.getIsRewardRecorded().getDesc();
        if(queryReq.getIsRewardRecorded().equals(RewardRecordedFlag.YES)){
             newColumns =  ArrayUtils.addAll(columns,isRewardRecordedColumns);
        }else if(queryReq.getIsRewardRecorded().equals(RewardRecordedFlag.NO)){
            newColumns =  ArrayUtils.addAll(columns,isNotRewardRecordedColumns);
        }

        excelHelper.addSheet(
                sheetName,
                newColumns,
                dataRecords
        );
        excelHelper.write(outputStream);

    }

}
