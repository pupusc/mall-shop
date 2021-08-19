package com.wanmi.sbc.distribution;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.SensitiveUtils;
import com.wanmi.sbc.common.util.SiteResultCode;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoSpecDetailRelVO;
import com.wanmi.sbc.marketing.api.provider.distributionrecord.DistributionRecordQueryProvider;
import com.wanmi.sbc.marketing.api.request.distributionrecord.DistributionRecordExportRequest;
import com.wanmi.sbc.marketing.bean.vo.DistributionRecordVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 分销记录导出接口
 * Created by of2975 on 2019/4/14.
 */
@Api(description = "分销记录导出API", tags = "BossDistributionRecordExportController")
@Slf4j
@RestController
@Validated
@RequestMapping(value = "/distribution/record")
public class DistributionRecordExportController {

    @Autowired
    private DistributionRecordQueryProvider distributionRecordQueryProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    private AtomicInteger exportCount = new AtomicInteger(0);

    /**
     * 导出分销记录
     * @param encrypted
     * @param response
     */
    @ApiOperation(value = "导出分销记录")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "解密", required = true)
    @RequestMapping(value = "/export/params/{encrypted}", method = RequestMethod.GET)
    public void export(@PathVariable String encrypted, HttpServletResponse response) {
        try {
            if (exportCount.incrementAndGet() > 1) {
                throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
            }

            String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
            DistributionRecordExportRequest queryReq = JSON.parseObject(decrypted, DistributionRecordExportRequest.class);
            // 商家端
            CommonUtil commonUtil = new CommonUtil();
            queryReq.setStoreId(commonUtil.getStoreId());
            // 导出数据查询
            List<DistributionRecordVO> dataRecords = distributionRecordQueryProvider.export(queryReq).getContext().getDistributionRecordVOList();

            String headerKey = "Content-Disposition";
            LocalDateTime dateTime = LocalDateTime.now();
            String fileName = String.format("批量导出分销记录_%s.xls", dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
            String fileNameNew = fileName;
            try {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("/distribution/record/export/params, fileName={},", fileName, e);
            }
            String headerValue = String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName);
            response.setHeader(headerKey, headerValue);


            try {
                export(dataRecords, response.getOutputStream(), queryReq);
                response.flushBuffer();
            } catch (IOException e) {
                throw new SbcRuntimeException(e);
            }
            operateLogMQUtil.convertAndSend("营销", "批量导出分销记录", fileNameNew);

        } catch (Exception e) {
            log.error("/distribution/customer/export/params error: ", e);
            throw new SbcRuntimeException(SiteResultCode.ERROR_000001);
        } finally {
            exportCount.set(0);
        }

    }

    /**
     * 分销记录导出
     * @param dataRecords
     * @param outputStream
     * @param queryReq
     */
    private void export(List<DistributionRecordVO> dataRecords, OutputStream outputStream, DistributionRecordExportRequest queryReq) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
                new Column("商品名称",
                        (cell, object) -> {
                            final StringBuilder sb = new StringBuilder();
                            DistributionRecordVO distributionRecord = (DistributionRecordVO) object;
                            sb.append(distributionRecord.getGoodsInfo().getGoodsInfoName());
                            List<GoodsInfoSpecDetailRelVO> goodsInfoSpecDetailRelVOS = distributionRecord.getGoodsInfoSpecDetailRelVOS();
                            if(CollectionUtils.isNotEmpty(goodsInfoSpecDetailRelVOS)){
                                goodsInfoSpecDetailRelVOS.forEach(v->sb.append(v.getDetailName()));

                            }
                            cell.setCellValue(sb.toString());
                }),
                new Column("商品sku编号", new SpelColumnRender<DistributionRecordVO>("goodsInfo.goodsInfoNo")),
                new Column("订单编号", new SpelColumnRender<DistributionRecordVO>("tradeId")),
                new Column("店铺名称", new SpelColumnRender<DistributionRecordVO>("storeVO.storeName")),
                new Column("店铺编号", new SpelColumnRender<DistributionRecordVO>("companyInfoVO.companyCode")),
                new Column("客户名称", new SpelColumnRender<DistributionRecordVO>("customerDetailVO.customerName")),
                new Column("客户账号", (cell, object) -> {
                    DistributionRecordVO distributionRecord = (DistributionRecordVO) object;
                    // 账号脱敏
                    cell.setCellValue(distributionRecord.getCustomerDetailVO()!=null?
                            SensitiveUtils.handlerMobilePhone(distributionRecord.getCustomerDetailVO().getCustomerVO().getCustomerAccount()):"");
                }),
                new Column("分销员名称", new SpelColumnRender<DistributionRecordVO>("distributionCustomerVO.customerName")),
                new Column("分销员账号", new SpelColumnRender<DistributionRecordVO>("distributionCustomerVO.customerAccount")),
                new Column("支付时间", new SpelColumnRender<DistributionRecordVO>("payTime")),
                new Column("订单完成时间", new SpelColumnRender<DistributionRecordVO>("finishTime")),
                new Column("佣金入账时间", new SpelColumnRender<DistributionRecordVO>("missionReceivedTime")),
                new Column("金额", (cell, object) -> {
                    DistributionRecordVO distributionRecord = (DistributionRecordVO) object;
                    if (Objects.nonNull(distributionRecord.getOrderGoodsPrice())) {
                        cell.setCellValue(distributionRecord.getOrderGoodsPrice().toString());
                    } else {
                        cell.setCellValue("0");
                    }
                }),
                new Column("数量", new SpelColumnRender<DistributionRecordVO>("orderGoodsCount")),
                new Column("佣金", (cell, object) -> {
                    DistributionRecordVO distributionRecord = (DistributionRecordVO) object;
                    if (Objects.nonNull(distributionRecord.getCommissionGoods())) {
                        cell.setCellValue(distributionRecord.getCommissionGoods().toString());
                    } else {
                        cell.setCellValue("0");
                    }
                }),
                // 单个货品佣金比例 默认0%
                new Column("比例", (cell, object) -> {
                    DistributionRecordVO distributionRecord = (DistributionRecordVO) object;
                    if (Objects.nonNull(distributionRecord.getCommissionRate())) {
                        cell.setCellValue(distributionRecord.getCommissionRate().multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString() + "%");
                    } else {
                        cell.setCellValue("0%");
                    }
                }),
        };

        String sheetName = "分销记录";
        if (Objects.nonNull(queryReq.getRecordIdList())) {
            sheetName = "分销记录导出";
        } else if (Objects.nonNull(queryReq.getCommissionState())) {
            // 佣金已入账 佣金未入账
            sheetName = queryReq.getCommissionState().getType();
        } else if (queryReq.getDeleteFlag().equals(DeleteFlag.YES)) {
            sheetName = "入账失败";
        }

        excelHelper.addSheet(
                sheetName,
                columns,
                dataRecords
        );
        excelHelper.write(outputStream);
    }

}

