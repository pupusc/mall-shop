package com.wanmi.sbc.providertrade;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.aop.EmployeeCheck;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.OrderType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.SiteResultCode;
import com.wanmi.sbc.order.api.provider.trade.ProviderTradeProvider;
import com.wanmi.sbc.order.api.provider.trade.ProviderTradeQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.request.trade.*;
import com.wanmi.sbc.order.bean.dto.ProviderTradeQueryDTO;
import com.wanmi.sbc.order.bean.dto.TradeDeliverDTO;
import com.wanmi.sbc.order.bean.dto.TradeDeliverRequestDTO;
import com.wanmi.sbc.order.bean.dto.TradeQueryDTO;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.ShipperType;
import com.wanmi.sbc.order.bean.vo.ProviderTradeExportVO;
import com.wanmi.sbc.order.bean.vo.TradeDeliverVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.order.request.TradeExportRequest;
import com.wanmi.sbc.providertrade.service.ProviderTradeExportService;
import com.wanmi.sbc.setting.api.provider.expresscompany.ExpressCompanyQueryProvider;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyByIdRequest;
import com.wanmi.sbc.setting.bean.vo.ExpressCompanyVO;
import com.wanmi.sbc.util.CommonUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Description: 供应商订单控制层
 * @Autho qiaokang
 * @Date：2020-03-27 14:34
 */
@Api(tags = "ProviderTradeController", description = "订单服务 Api")
@RestController
@RequestMapping("/providerTrade")
@Slf4j
@Validated
public class ProviderTradeController {

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private ProviderTradeQueryProvider providerTradeQueryProvider;

    @Autowired
    private ProviderTradeProvider providerTradeProvider;

    @Autowired
    private ExpressCompanyQueryProvider expressCompanyQueryProvider;

    @Autowired
    private TradeProvider tradeProvider;

    @Autowired
    private ProviderTradeExportService providerTradeExportService;

    private AtomicInteger exportCount = new AtomicInteger(0);

    /**
     * 分页查询
     *
     * @param tradeQueryRequest
     * @return
     */
    @ApiOperation(value = "分页查询")
    @EmployeeCheck
    @RequestMapping(method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<TradeVO>> page(@RequestBody ProviderTradeQueryDTO tradeQueryRequest) {
        Long companyInfoId = commonUtil.getCompanyInfoId();
        if (companyInfoId != null) {
            tradeQueryRequest.setSupplierId(companyInfoId);
        }

        // 供应商订单列表要过滤待审核和待成团的订单
        tradeQueryRequest.setNotFlowStates(Arrays.asList(FlowState.INIT, FlowState.GROUPON));
        //设定状态条件逻辑,已审核状态需筛选出已审核与部分发货
        tradeQueryRequest.makeAllAuditFlow();
        tradeQueryRequest.setOrderType(OrderType.ALL_ORDER);

        return BaseResponse.success(providerTradeQueryProvider.providerPageCriteria(
                ProviderTradePageCriteriaRequest.builder().tradePageDTO(tradeQueryRequest).build()).getContext().getTradePage());
    }

    /**
     * 查看订单详情
     *
     * @param tid
     * @return
     */
    @ApiOperation(value = "查看订单详情")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单id", required = true)
    @RequestMapping(value = "/{tid}", method = RequestMethod.GET)
    public BaseResponse<TradeVO> detail(@PathVariable String tid) {
        TradeVO trade = providerTradeQueryProvider.providerGetById(TradeGetByIdRequest.builder().tid(tid).build()).getContext().getTradeVO();
        return BaseResponse.success(trade);
    }

    /**
     * 发货
     *
     * @param tid
     * @return
     */
    @ApiOperation(value = "发货")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单id", required = true)
    @RequestMapping(value = "/deliver/{tid}", method = RequestMethod.PUT)
    @GlobalTransactional
    public ResponseEntity<BaseResponse> deliver(@PathVariable String tid, @Valid @RequestBody TradeDeliverRequestDTO
            tradeDeliverRequest) {
        if (tradeDeliverRequest.getShippingItemList().isEmpty() && tradeDeliverRequest.getGiftItemList().isEmpty()) {
            throw new SbcRuntimeException("K-050314");
        }

        TradeDeliveryCheckRequest tradeDeliveryCheckRequest = TradeDeliveryCheckRequest.builder()
                .tid(tid)
                .tradeDeliver(tradeDeliverRequest)
                .build();

        providerTradeProvider.providerDeliveryCheck(tradeDeliveryCheckRequest);

        // 发货校验
        ExpressCompanyByIdRequest request = new ExpressCompanyByIdRequest();
        request.setExpressCompanyId(Long.valueOf(tradeDeliverRequest.getDeliverId()));
        ExpressCompanyVO expressCompanyVO = expressCompanyQueryProvider.getById(request).getContext().getExpressCompanyVO();
        TradeDeliverVO tradeDeliver = tradeDeliverRequest.toTradeDevlier(expressCompanyVO);
        tradeDeliver.setShipperType(ShipperType.PROVIDER);

        TradeDeliverRequest deliverRequest = TradeDeliverRequest.builder()
                .tradeDeliver(KsBeanUtil.convert(tradeDeliver, TradeDeliverDTO.class))
                .tid(tid)
                .operator(commonUtil.getOperator())
                .build();
        // 供应商发货处理
        String deliverId = providerTradeProvider.providerDeliver(deliverRequest).getContext().getDeliverId();

        //此处mongo事务无法控制，将逻辑移至子单发货逻辑中
        // 供应商订单信息
        /*TradeVO privateTradeVO = providerTradeQueryProvider.providerGetById(
                TradeGetByIdRequest.builder()
                        .tid(tid)
                        .build())
                .getContext()
                .getTradeVO();

        // 查询所有子订单信息
        List<TradeVO> tradeVOList = providerTradeQueryProvider.getProviderListByParentId(
                TradeListByParentIdRequest.builder()
                        .parentTid(privateTradeVO.getParentId())
                        .build())
                .getContext()
                .getTradeVOList();

        // 未发货订单数
        long notYetShippedNum = tradeVOList.stream().filter(tradeVO ->
                !tradeVO.getTradeState().getFlowState().equals(FlowState.VOID) &&
                        tradeVO.getTradeState().getDeliverStatus().equals(DeliverStatus.NOT_YET_SHIPPED))
                .count();
        // 部分发货
        long partShippedNum = tradeVOList.stream().filter(tradeVO ->
                !tradeVO.getTradeState().getFlowState().equals(FlowState.VOID) &&
                        tradeVO.getTradeState().getDeliverStatus().equals(DeliverStatus.PART_SHIPPED))
                .count();

        // 已发货订单数
        long shippedNum = tradeVOList.stream().filter(tradeVO -> tradeVO.getTradeState().getDeliverStatus().equals(DeliverStatus.SHIPPED)).count();

        // 父订单发货状态（此处的订单状态的赋值应该不起作用，在后面被重置为DeliverStatus.NOT_YET_SHIPPED）
        DeliverStatus deliverStatus;
        if ((int) notYetShippedNum == tradeVOList.size()){
            deliverStatus = DeliverStatus.NOT_YET_SHIPPED;
        } else if ((int) shippedNum == tradeVOList.size()||(notYetShippedNum == NumberUtils.LONG_ZERO && partShippedNum == NumberUtils.LONG_ZERO)) {
            deliverStatus = DeliverStatus.SHIPPED;
        } else {
            deliverStatus = DeliverStatus.PART_SHIPPED;
        }

        TradeDeliverDTO parentTradeDeliverDTO = KsBeanUtil.convert(tradeDeliver, TradeDeliverDTO.class);
        parentTradeDeliverDTO.setStatus(deliverStatus);
        parentTradeDeliverDTO.setSunDeliverId(deliverId);
        parentTradeDeliverDTO.setShipperType(ShipperType.PROVIDER);

        // 添加商家发货信息
        tradeProvider.deliver(TradeDeliverRequest.builder()
                .tradeDeliver(parentTradeDeliverDTO)
                .tid(privateTradeVO.getParentId())
                .operator(commonUtil.getOperator())
                .build());*/

        return ResponseEntity.ok(BaseResponse.success(deliverId));
    }

    /**
     * 导出订单
     *
     * @return
     */
    @ApiOperation(value = "导出订单")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "解密", required = true)
    @RequestMapping(value = "/export/params/{encrypted}", method = RequestMethod.GET)
    public void export(@PathVariable String encrypted, HttpServletResponse response) {
        try {
            if (exportCount.incrementAndGet() > 1) {
                throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
            }

            String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
            TradeExportRequest tradeExportRequest = JSON.parseObject(decrypted, TradeExportRequest.class);

            Operator operator = commonUtil.getOperator();
            log.debug(String.format("/prioviderTrade/export/params, employeeId=%s", operator.getUserId()));

            String adminId = operator.getAdminId();
            if (StringUtils.isNotEmpty(adminId)) {
                tradeExportRequest.setSupplierId(Long.valueOf(adminId));
            }

            //设定状态条件逻辑,已审核状态需筛选出已审核与部分发货
            tradeExportRequest.makeAllAuditFlow();
            TradeQueryDTO tradeQueryDTO = KsBeanUtil.convert(tradeExportRequest, TradeQueryDTO.class);
            List<TradeVO> trades = providerTradeQueryProvider.providerListTradeExport(
                    TradeListExportRequest.builder().tradeQueryDTO(tradeQueryDTO).build()).getContext().getTradeVOList();

            //按下单时间降序排列
            Comparator<TradeVO> c = Comparator.comparing(a -> a.getTradeState().getCreateTime());
            trades = trades.stream().sorted(
                    c.reversed()
            ).collect(Collectors.toList());

            // 遍历封装导出信息
            List<ProviderTradeExportVO> tradeExportVOs = new ArrayList<>();
            trades.forEach(tradeVO -> {
                ProviderTradeExportVO exportVO;
                // 商家信息
                String supplierName = StringUtils.isNotEmpty(tradeVO.getSupplierName()) ? tradeVO.getSupplierName() : "";
                String supplierCode = StringUtils.isNotEmpty(tradeVO.getSupplierCode()) ? tradeVO.getSupplierCode() : "";
                String supplierInfo = supplierName + "  " + supplierCode;
                for (int i = 0; i < tradeVO.getTradeItems().size(); i++) {
                    TradeItemVO tradeItemVO = tradeVO.getTradeItems().get(i);
                    exportVO = new ProviderTradeExportVO();

                    KsBeanUtil.copyProperties(tradeVO, exportVO);
                    // 下单时间
                    exportVO.setCreateTime(tradeVO.getTradeState().getCreateTime());
                    // 商家信息
                    exportVO.setSupplierInfo(supplierInfo);
                    // 订单商品金额
                    exportVO.setOrderGoodsPrice(tradeItemVO.getTotalSupplyPrice());
                    // 订单状态
                    exportVO.setFlowState(tradeVO.getTradeState().getFlowState());
                    // 发货状态
                    exportVO.setDeliverStatus(tradeVO.getTradeState().getDeliverStatus());
                    exportVO.setConsigneeName(tradeVO.getConsignee().getName());
                    exportVO.setDetailAddress(tradeVO.getConsignee().getDetailAddress());
                    exportVO.setConsigneePhone(tradeVO.getConsignee().getPhone());
                    exportVO.setSkuName(tradeItemVO.getSkuName());
                    exportVO.setSpecDetails(tradeItemVO.getSpecDetails());
                    exportVO.setSkuNo(tradeItemVO.getSkuNo());
                    exportVO.setNum(tradeItemVO.getNum());
                    exportVO.setPayState(tradeVO.getTradeState().getPayState());
                    exportVO.setTotalNum(tradeVO.getTradeItems().stream().collect(Collectors.summingLong(TradeItemVO::getNum)));
                    exportVO.setGoodsSpecies(Long.valueOf(tradeVO.getTradeItems().stream().map(TradeItemVO::getCateId).collect(Collectors.toSet()).size()));
                    tradeExportVOs.add(exportVO);
                }
            });

            String headerKey = "Content-Disposition";
            LocalDateTime dateTime = LocalDateTime.now();
            String fileName = String.format("批量导出订单_%s.xls", dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
            try {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("/prioviderTrade/export/params, fileName={},", fileName, e);
            }
            String headerValue = String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName);
            response.setHeader(headerKey, headerValue);

            try {
                providerTradeExportService.export(tradeExportVOs, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                throw new SbcRuntimeException(e);
            }
        } catch (Exception e) {
            log.error("/prioviderTrade/export/params error: ", e);
            throw new SbcRuntimeException(SiteResultCode.ERROR_000001);
        } finally {
            exportCount.set(0);
        }
    }

    /**
     * 发货作废
     *
     * @param tid
     * @param tdId
     * @return
     */
    @ApiOperation(value = "发货作废")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "tid", value = "订单Id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "tdId", value = "发货单Id", required = true)
    })
    @RequestMapping(value = "/deliver/{tid}/void/{tdId}", method = RequestMethod.GET)
    @GlobalTransactional
    public ResponseEntity<BaseResponse> deliverVoid(@PathVariable String tid, @PathVariable String tdId,
                                                    HttpServletRequest req) {

        providerTradeProvider.deliverRecordObsolete(TradeDeliverRecordObsoleteRequest.builder()
                .deliverId(tdId).tid(tid).operator(commonUtil.getOperator()).build());
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }
}
