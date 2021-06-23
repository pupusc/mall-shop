package com.wanmi.sbc.finance;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.elastic.api.provider.settlement.EsSettlementProvider;
import com.wanmi.sbc.elastic.api.provider.settlement.EsSettlementQueryProvider;
import com.wanmi.sbc.elastic.api.request.settlement.EsSettlementPageRequest;
import com.wanmi.sbc.elastic.api.request.settlement.SettlementQueryRequest;
import com.wanmi.sbc.elastic.api.response.settlement.EsSettlementResponse;
import com.wanmi.sbc.elastic.bean.vo.settlement.EsSettlementVO;
import com.wanmi.sbc.order.api.provider.settlement.SettlementDetailQueryProvider;
import com.wanmi.sbc.account.api.provider.finance.record.SettlementProvider;
import com.wanmi.sbc.account.api.provider.finance.record.SettlementQueryProvider;
import com.wanmi.sbc.account.api.request.finance.record.*;
import com.wanmi.sbc.order.api.response.settlement.SettlementDetailListBySettleUuidResponse;
import com.wanmi.sbc.account.api.response.finance.record.SettlementGetViewResponse;
import com.wanmi.sbc.account.api.response.finance.record.SettlementToExcelResponse;
import com.wanmi.sbc.account.bean.dto.SettlementDTO;
import com.wanmi.sbc.account.bean.enums.SettleStatus;
import com.wanmi.sbc.account.bean.vo.*;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.SpanColumn;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.order.api.request.settlement.SettlementDetailListBySettleUuidRequest;
import com.wanmi.sbc.order.bean.vo.SettlementDetailVO;
import com.wanmi.sbc.order.bean.vo.SettlementDetailViewVO;
import com.wanmi.sbc.setting.api.provider.yunservice.YunServiceProvider;
import com.wanmi.sbc.setting.api.request.yunservice.YunGetResourceRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunUploadResourceRequest;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Api(tags = "SettlementController", description = "结算单 Api")
@RestController
@RequestMapping("/finance/settlement")
public class SettlementController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SettlementProvider settlementProvider;

    @Autowired
    private SettlementQueryProvider settlementQueryProvider;

    @Autowired
    private SettlementDetailQueryProvider settlementDetailQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private YunServiceProvider yunServiceProvider;

    @Autowired
    private EsSettlementQueryProvider esSettlementQueryProvider;

    @Autowired
    private EsSettlementProvider esSettlementProvider;

    public static final String NOT_SETTLED = "未结算";

    public static final String SETTLED = "已结算";

    public static final String SETTLE_LATER = "暂不处理";

    public static final String EXCEL_NAME = "财务结算";

    public static final String EXCEL_TYPE = "xls";

    /**
     * 分页查询结算单(商家)
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "分页查询结算单")
    @RequestMapping(value = "/oldPage", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<SettlementViewVO>> pageBasePageRequest(@RequestBody SettlementPageRequest request) {
        if (commonUtil.getStoreId() != null) {
            request.setStoreId(commonUtil.getStoreId());
        }
        request.setStoreType(StoreType.SUPPLIER);
        return BaseResponse.success(settlementQueryProvider.page(request).getContext().getSettlementViewVOPage());
    }

    /**
     * 分页查询结算单(商家) 从es查找
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "分页查询结算单")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<EsSettlementVO>> pageBasePageRequestFromEs(@RequestBody EsSettlementPageRequest request) {
        if (commonUtil.getStoreId() != null) {
            request.setStoreId(commonUtil.getStoreId());
        }
        request.setStoreType(StoreType.SUPPLIER);
        BaseResponse<EsSettlementResponse> response = esSettlementQueryProvider.esSettlementPage(request);
        if(Objects.nonNull(response.getContext())){
            return BaseResponse.success(response.getContext().getEsSettlementVOPage());
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 更改结算单状态
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "更改结算单状态")
    @RequestMapping(value = "/status", method = RequestMethod.PUT)
    public BaseResponse changeSettlementStatus(@RequestBody SettlementBatchModifyStatusRequest request) {
        List<Long> settleIdList = request.getSettleIdList();

        SettleStatus status = request.getStatus();
        settlementProvider.batchModifyStatus(request);
        esSettlementProvider.updateSettlementStatus(SettlementQueryRequest.builder().settleIdLists(settleIdList).status(status.toValue()).build());
        //操作日志记录
        if (SettleStatus.SETTLE_LATER.equals(status)) {
            if (CollectionUtils.size(settleIdList) == 1) {
                operateLogMQUtil.convertAndSend("财务", "暂不处理",
                        "暂不处理：结算单号" + String.format("S%07d", settleIdList.get(0)));
            } else {
                operateLogMQUtil.convertAndSend("财务", "批量暂不处理", "批量暂不处理");
            }
        } else if (SettleStatus.SETTLED.equals(status)) {
            if (CollectionUtils.size(settleIdList) == 1) {
                operateLogMQUtil.convertAndSend("财务", "设为已结算",
                        "设为已结算：结算单号" + String.format("S%07d", settleIdList.get(0)));
            } else {
                operateLogMQUtil.convertAndSend("财务", "批量设为已结算", "批量设为已结算");
            }
        }

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询结算明细
     *
     * @param settleId
     * @return
     */
    @ApiOperation(value = "查询结算明细")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "settleId", value = "结算Id", required = true)
    @RequestMapping(value = "/detail/list/{settleId}", method = RequestMethod.GET)
    public BaseResponse<List<SettlementDetailViewVO>> getSettlementDetailList(@PathVariable("settleId") Long settleId) {
        SettlementVO settlement = settlementQueryProvider.getById(
                SettlementGetByIdRequest.builder().settleId(settleId).build()
        ).getContext();
        if (settlement != null) {
            BaseResponse<SettlementDetailListBySettleUuidResponse> baseResponse =
                    settlementDetailQueryProvider.listBySettleUuid(new SettlementDetailListBySettleUuidRequest(settlement.getSettleUuid()));
            SettlementDetailListBySettleUuidResponse response = baseResponse.getContext();
            if (Objects.nonNull(response)) {
                List<SettlementDetailVO> settlementDetailVOList = response.getSettlementDetailVOList();
                return BaseResponse.success(SettlementDetailViewVO.renderSettlementDetailForView(settlementDetailVOList, false));
            }
            throw new SbcRuntimeException("K-020201");
        } else {
            throw new SbcRuntimeException("K-020201");
        }
    }

    /**
     * 查询结算明细
     *
     * @param encrypted
     * @param response
     */
    @ApiOperation(value = "查询结算明细")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "加密", required = true)
    @RequestMapping(value = "/detail/export/{encrypted}", method = RequestMethod.GET)
    public void exportSettlementDetailList(@PathVariable("encrypted") String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        JSONObject jsonObject = JSONObject.parseObject(decrypted);
        if (jsonObject.get("settleId") != null) {
            SettlementVO settlement = settlementQueryProvider.getById(
                    SettlementGetByIdRequest.builder().settleId(Long.parseLong(jsonObject.get("settleId").toString())).build()
            ).getContext();
            if (settlement != null) {

                String headerKey = "Content-Disposition";
                String fileName = String.format("结算明细_%s-%s.xls", settlement.getStartTime(), settlement.getEndTime());
                try {
                    fileName = URLEncoder.encode(fileName, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    logger.error("/detail/export/{}, error={}", encrypted, e);
                }
                String headerValue = String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName,
                        fileName);
                response.setHeader(headerKey, headerValue);
                try {
                    SettlementDTO settlementDTO = new SettlementDTO();
                    KsBeanUtil.copyPropertiesThird(settlement, settlementDTO);
                    exportSettlementDetail(settlementDTO, response.getOutputStream());
                    response.flushBuffer();
                } catch (IOException e) {
                    throw new SbcRuntimeException(e);
                }
            } else {
                throw new SbcRuntimeException("K-020201");
            }
        } else {
            throw new SbcRuntimeException();
        }
    }


    /**
     * 导出结算明细
     *
     * @param settlement
     * @param outputStream
     */
    private void exportSettlementDetail(SettlementDTO settlement, OutputStream outputStream) {
        String fileKey = "settlement/" + settlement.getStoreId() + "/" + settlement.getSettleUuid() + ".xls";
        byte[] content = yunServiceProvider.getFile(YunGetResourceRequest.builder()
                .resourceKey(fileKey)
                .build()).getContext().getContent();
        if (content == null) {
            SettlementDetailListBySettleUuidRequest settlementDetailListBySettleUuidRequest =
                    new SettlementDetailListBySettleUuidRequest();
            settlementDetailListBySettleUuidRequest.setSettleUuid(settlement.getSettleUuid());
            BaseResponse<SettlementDetailListBySettleUuidResponse> baseResponse =
                    settlementDetailQueryProvider.listBySettleUuid(settlementDetailListBySettleUuidRequest);
            SettlementDetailListBySettleUuidResponse settlementDetailListBySettleUuidResponse =
                    baseResponse.getContext();
            if (Objects.isNull(settlementDetailListBySettleUuidResponse)) {
                return;
            }
            List<SettlementDetailVO> settlementDetailVOList =
                    settlementDetailListBySettleUuidResponse.getSettlementDetailVOList();

            // List<SettlementDetail> detailList = this.getSettlementDetail();
            List<SettlementDetailViewVO> viewList =
                    SettlementDetailViewVO.renderSettlementDetailForView(settlementDetailVOList, true);
            ExcelHelper<SettlementDetailViewVO> excelHelper = new ExcelHelper<>();
            excelHelper.addSheet("结算明细", new SpanColumn[]{
                    new SpanColumn("序号", "index", null),
                    new SpanColumn("订单入账时间", "finalTime", null),
                    new SpanColumn("订单编号", "tradeCode", null),
                    new SpanColumn("订单类型", "orderType", null),
                    new SpanColumn("商品编码/名称/规格", "goodsViewList", "goodsName"),
                    new SpanColumn("所属类目", "goodsViewList", "cateName"),
                    new SpanColumn("商品单价", "goodsViewList", "goodsPrice"),
                    new SpanColumn("供货金额", "goodsViewList", "providerPrice"),
                    new SpanColumn("数量", "goodsViewList", "num"),
                    new SpanColumn("满减优惠", "goodsViewList", "reductionPrice"),
                    new SpanColumn("满折优惠", "goodsViewList", "discountPrice"),
                    new SpanColumn("店铺券优惠", "goodsViewList", "storeCouponPrice"),
                    new SpanColumn("通用券优惠", "goodsViewList", "commonCouponPriceString"),
                    new SpanColumn("订单改价差额", "goodsViewList", "specialPrice"),
                    new SpanColumn("积分抵扣", "goodsViewList", "pointPrice"),
                    new SpanColumn("商品实付金额", "goodsViewList", "splitPayPrice"),
                    new SpanColumn("类目扣率", "goodsViewList", "cateRate"),
                    new SpanColumn("平台佣金", "goodsViewList", "platformPriceString"),
                    new SpanColumn("分销佣金", "goodsViewList", "commission"),
                    new SpanColumn("运费", "deliveryPrice", null),
                    new SpanColumn("退单改价差额", "returnSpecialPrice", null),
                    new SpanColumn("店铺应收金额", "storePrice", null),
                    new SpanColumn("供货总额", "providerPrice", null),
            }, viewList, "goodsViewList");

            //向response写入流
            excelHelper.write(outputStream);

            //如果excel文件内容为空不上传至云，后期可以设置超过一定的长度再上传云
            if (viewList.size() > 1000) {
                //写入流
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                excelHelper.write(byteArrayOutputStream);
                yunServiceProvider.uploadFile(YunUploadResourceRequest.builder()
                        .resourceName(fileKey)
                        .content(byteArrayOutputStream.toByteArray())
                        .build());
            }
        } else {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
//            OutputStream excelOutputStream = new ByteArrayOutputStream();
//            int rc;
//            while ((rc = byteArrayInputStream.read()) != -1) {
//                try {
//                    excelOutputStream.write(rc);
//                } catch (IOException e) {
//                    throw new SbcRuntimeException(e);
//                }
//            }
//
//            ByteArrayInputStream inputStream = new ByteArrayInputStream(((ByteArrayOutputStream) excelOutputStream)
//                    .toByteArray());
            try {
                int ch;
                while ((ch = inputStream.read()) != -1) {
                    outputStream.write(ch);
                }
            } catch (IOException e) {
                logger.error("excel write error", e);
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
//                    if (excelOutputStream != null) {
//                        excelOutputStream.close();
//                    }
                } catch (IOException e) {
                    logger.error("excel write error", e);
                }
            }
        }

    }

    /**
     * 查询结算单信息
     *
     * @param settleId
     * @return
     */
    @ApiOperation(value = "查询结算单信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "settleId", value = "结算ID", required = true)
    @RequestMapping(value = "/{settleId}", method = RequestMethod.GET)
    public BaseResponse<SettlementViewVO> getSettlementById(@PathVariable("settleId") Long settleId) {
        SettlementVO settlement = settlementQueryProvider.getById(
                SettlementGetByIdRequest.builder().settleId(settleId).build()
        ).getContext();
        if (settlement != null) {
            SettlementGetViewRequest request = KsBeanUtil.convert(settlement, SettlementGetViewRequest.class);
            BaseResponse<SettlementGetViewResponse> view = settlementQueryProvider.getView(request);
            return BaseResponse.success(view.getContext());
        } else {
            throw new SbcRuntimeException("K-020201");
        }
    }

    /**
     * 获得店铺总的结算资金、待结算资金
     *
     * @return
     */
    @ApiOperation(value = "获得店铺总的结算资金、待结算资金")
    @RequestMapping(value = "/queryToTalSettlement", method = RequestMethod.GET)
    public BaseResponse<List<SettlementTotalVO>> queryToTalSettlement() {
        return BaseResponse.success(settlementQueryProvider.countByStoreId(
                SettlementTotalByStoreIdRequest.builder().storeId(commonUtil.getStoreId()).build()
        ).getContext().getSettlementTotalVOList());
    }


    /**
     * 财务结算导出
     *
     * @param encrypted
     * @throws Exception
     */
    @ApiOperation(value = "财务结算导出")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "加密", required = true)
    @GetMapping(value = "/export/{encrypted}")
    public void exportIncome(@PathVariable String encrypted) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        SettlementToExcelRequest request = JSON.parseObject(decrypted, SettlementToExcelRequest.class);

        if (!ObjectUtils.isEmpty(commonUtil.getStoreId())) {
            request.setStoreId(commonUtil.getStoreId());
        }

        // 财务结算报表导出数据
        SettlementToExcelResponse excelResponse = settlementQueryProvider.getSettlementExportData(request).getContext();

        String fileName = getFileName(request);
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            // 写入到response
            HttpUtil.getResponse().setContentType("application/vnd.ms-excel");
            HttpUtil.getResponse().setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";" +
                    "filename*=\"utf-8''%s\"", fileName, fileName));
            // 写入Excel
            writeSettleToExcel(excelResponse, HttpUtil.getResponse().getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 按照财务结算状态写入Excel
     *
     * @param settlementViewExcel 财务结算报表导出数据
     * @param outputStream
     */
    private void writeSettleToExcel(SettlementToExcelResponse settlementViewExcel, OutputStream outputStream) {
        ExcelHelper helper = new ExcelHelper();
        //写入未结算数据
        doExportSettle(helper, settlementViewExcel.getNotSettledSettlements(), NOT_SETTLED);

        //写入已结算数据
        doExportSettle(helper, settlementViewExcel.getSettledSettlements(), SETTLED);

        //导出文件
        helper.write(outputStream);
    }

    /**
     * 财务结算报表写入数据
     *
     * @param settlementViewList
     * @param helper
     * @param sheetName
     */
    @SuppressWarnings("unchecked")
    private void doExportSettle(ExcelHelper helper, List<SettlementViewVO>
            settlementViewList, String sheetName) {
        List<Column> columnList = new ArrayList<>();
        if (sheetName.equals(SETTLED)) {
            columnList.add(new Column("结算时间", (cell, object) -> {
                SettlementViewVO settlementView = (SettlementViewVO) object;
                String cellValue = DateUtil.format(settlementView.getSettleTime(), DateUtil.FMT_DATE_1);
                cell.setCellValue(StringUtils.defaultString(cellValue));
            }));
        }
        columnList.add(new Column("生成时间", (cell, object) -> {
            SettlementViewVO settlementView = (SettlementViewVO) object;
            String cellValue = DateUtil.format(settlementView.getCreateTime(), DateUtil.FMT_DATE_1);
            cell.setCellValue(StringUtils.defaultString(cellValue));
        }));
        columnList.add(new Column("结算单号", new SpelColumnRender<SettlementViewVO>("settlementCode")));
        columnList.add(new Column("结算时间段", (cell, object) -> {
            SettlementViewVO settlementView = (SettlementViewVO) object;
            String startTime = settlementView.getStartTime();
            String endTime = settlementView.getEndTime();
            String cellValue = startTime +
                    "～" +
                    endTime;
            cell.setCellValue(cellValue);
        }));
        columnList.add(new Column("店铺名称", new SpelColumnRender<SettlementViewVO>("storeName")));
        columnList.add(new Column("商品实付总额", new SpelColumnRender<SettlementViewVO>("splitPayPrice")));
        columnList.add(new Column("运费总额", new SpelColumnRender<SettlementViewVO>("deliveryPrice")));
        columnList.add(new Column("通用券优惠总额", new SpelColumnRender<SettlementViewVO>("commonCouponPrice")));
        columnList.add(new Column("积分抵扣总额", new SpelColumnRender<SettlementViewVO>("pointPrice")));
        columnList.add(new Column("平台佣金总额", new SpelColumnRender<SettlementViewVO>("platformPrice")));
        columnList.add(new Column("分销佣金总额", new SpelColumnRender<SettlementViewVO>("commissionPrice")));
        columnList.add(new Column("供货总额", new SpelColumnRender<SettlementViewVO>("providerPrice")));
        columnList.add(new Column("店铺应收总额", new SpelColumnRender<SettlementViewVO>("storePrice")));
        columnList.add(new Column("结算状态", (cell, object) -> {
            SettlementViewVO settlementView = (SettlementViewVO) object;
            SettleStatus settleStatus = settlementView.getSettleStatus();
            Integer status = settleStatus.toValue();
            if (status == BigDecimal.ROUND_UP) {
                cell.setCellValue(NOT_SETTLED);
            } else if (status == BigDecimal.ROUND_DOWN) {
                cell.setCellValue(SETTLED);
            } else if (status == BigDecimal.ROUND_CEILING) {
                cell.setCellValue(SETTLE_LATER);
            }
        }));
        helper.addSheet(sheetName, columnList.toArray(new Column[columnList.size()]), settlementViewList);
    }

    /**
     * 设置导出文件名
     *
     * @param request
     * @return
     */
    private String getFileName(SettlementToExcelRequest request) {
        String startTime = request.getStartTime();
        String endTime = request.getEndTime();
        String fileName = EXCEL_NAME + startTime + "～" + endTime + "." + EXCEL_TYPE;
        return fileName;
    }
}
