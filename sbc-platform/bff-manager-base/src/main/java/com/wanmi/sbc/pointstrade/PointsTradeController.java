package com.wanmi.sbc.pointstrade;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.aop.EmployeeCheck;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.SiteResultCode;
import com.wanmi.sbc.order.api.provider.pointstrade.PointsTradeProvider;
import com.wanmi.sbc.order.api.provider.pointstrade.PointsTradeQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.ProviderTradeProvider;
import com.wanmi.sbc.order.api.request.pointstrade.*;
import com.wanmi.sbc.order.api.request.trade.FindProviderTradeRequest;
import com.wanmi.sbc.order.bean.dto.*;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.vo.PointsTradeVO;
import com.wanmi.sbc.order.bean.vo.TradeDeliverVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.order.request.DisabledExportRequest;
import com.wanmi.sbc.pointstrade.request.PointsTradeExportRequest;
import com.wanmi.sbc.pointstrade.service.PointsTradeExportService;
import com.wanmi.sbc.setting.api.provider.expresscompany.ExpressCompanyQueryProvider;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyByIdRequest;
import com.wanmi.sbc.setting.bean.vo.ExpressCompanyVO;
import com.wanmi.sbc.util.CommonUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
 * @ClassName PointsTradeController
 * @Description 积分订单服务 Api
 * @Author lvzhenwei
 * @Date 2019/5/10 14:16
 **/
@Api(tags = "PointsTradeController", description = "积分订单服务 Api")
@RestController
@RequestMapping("/points/trade")
@Slf4j
@Validated
public class PointsTradeController {

    @Autowired
    private PointsTradeQueryProvider pointsTradeQueryProvider;

    @Autowired
    private PointsTradeExportService pointsTradeExportService;

    @Autowired
    private PointsTradeProvider pointsTradeProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private ProviderTradeProvider providerTradeProvider;


    @Autowired
    private ExpressCompanyQueryProvider expressCompanyQueryProvider;

    private AtomicInteger exportCount = new AtomicInteger(0);

    /**
     * @Author lvzhenwei
     * @Description 根据查询条件分页查询积分订单列表信息
     * @Date 11:26 2019/5/27
     * @Param [pointsTradeQueryRequest]
     **/
    @ApiOperation(value = "根据查询条件分页查询积分订单列表信息")
    @EmployeeCheck
    @RequestMapping(method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<PointsTradeVO>> page(@RequestBody PointsTradeQueryDTO pointsTradeQueryRequest) {
        Long companyInfoId = commonUtil.getCompanyInfoId();
        if (companyInfoId != null) {
            pointsTradeQueryRequest.setSupplierId(companyInfoId);
        }
        if (!pointsTradeQueryRequest.getIsBoss()) {
            if (Objects.nonNull(pointsTradeQueryRequest.getTradeState()) && Objects.nonNull(pointsTradeQueryRequest.getTradeState
                    ().getPayState())) {
                pointsTradeQueryRequest.setNotFlowStates(Arrays.asList(FlowState.VOID, FlowState.INIT));
            }
        }
        //设定状态条件逻辑,已审核状态需筛选出已审核与部分发货
        pointsTradeQueryRequest.makeAllAuditFlow();
        return BaseResponse.success(pointsTradeQueryProvider.pageCriteria(PointsTradePageCriteriaRequest.builder().
                pointsTradePageDTO(pointsTradeQueryRequest).build()).getContext().getPointsTradePage());
    }

    /**
     * @return com.wanmi.sbc.common.base.BaseResponse<com.wanmi.sbc.order.bean.vo.PointsTradeVO>
     * @Author lvzhenwei
     * @Description 查看积分订单详情
     * @Date 15:34 2019/5/10
     * @Param [tid]
     **/
    @ApiOperation(value = "查看积分订单详情")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "积分订单id", required = true)
    @RequestMapping(value = "/{tid}", method = RequestMethod.GET)
    public BaseResponse<PointsTradeVO> detail(@PathVariable String tid) {
        PointsTradeVO pointsTradeVO =
                pointsTradeQueryProvider.getById(PointsTradeGetByIdRequest.builder().tid(tid).needLmOrder(Boolean.TRUE).build()).getContext().getPointsTradeVo();
        return BaseResponse.success(pointsTradeVO);
    }

    /**
     * 发货
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
        PointsTradeDeliveryCheckRequest pointsTradeDeliveryCheckRequest = PointsTradeDeliveryCheckRequest.builder()
                .tid(tid)
                .tradeDeliver(tradeDeliverRequest)
                .build();
        pointsTradeProvider.deliveryCheck(pointsTradeDeliveryCheckRequest);
        //发货校验
//        ExpsComQueryRopRequest queryRopRequest = new ExpsComQueryRopRequest();
//        queryRopRequest.setExpressCompanyId(Long.valueOf(tradeDeliverRequest.getDeliverId()));
//        CompositeResponse<ExpressCompany> response
//                = sdkClient.buildClientRequest().post(queryRopRequest, ExpressCompany.class, "expressCompany.detail",
//                "1.0.0");
//        if (!response.isSuccessful()) {
//            throw new SbcRuntimeException(ResultCode.FAILED);
//        }
//        TradeDeliverVO tradeDeliver = tradeDeliverRequest.toTradeDevlier(response.getSuccessResponse());
        ExpressCompanyByIdRequest request = new ExpressCompanyByIdRequest();
        request.setExpressCompanyId(Long.valueOf(tradeDeliverRequest.getDeliverId()));
        ExpressCompanyVO expressCompanyVO = expressCompanyQueryProvider.getById(request).getContext().getExpressCompanyVO();
        TradeDeliverVO tradeDeliver = tradeDeliverRequest.toTradeDevlier(expressCompanyVO);
        PointsTradeDeliverRequest pointsTradeDeliverRequest = PointsTradeDeliverRequest.builder()
                .tradeDeliver(KsBeanUtil.convert(tradeDeliver, TradeDeliverDTO.class))
                .tid(tid)
                .operator(commonUtil.getOperator())
                .build();
        String deliverId = pointsTradeProvider.deliver(pointsTradeDeliverRequest).getContext().getDeliverId();
        return ResponseEntity.ok(BaseResponse.success(deliverId));
    }

    /**
     * 确认收货
     * @param tid
     * @return
     */
    @ApiOperation(value = "确认收货")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单Id", required = true)
    @RequestMapping(value = "/confirm/{tid}", method = RequestMethod.GET)
    @GlobalTransactional
    public ResponseEntity<BaseResponse> confirm(@PathVariable String tid) {
        pointsTradeProvider.confirmReceive(PointsTradeConfirmReceiveRequest.builder().tid(tid).operator(commonUtil.getOperator()).build());
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 积分订单导出
     * @Date 15:34 2019/5/10
     * @Param [encrypted, response]
     **/
    @ApiOperation(value = "积分订单导出")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "解密", required = true)
    @RequestMapping(value = "/export/params/{encrypted}/{encryptedable}", method = RequestMethod.GET)
    public void export(@PathVariable String encrypted,@PathVariable String encryptedable,HttpServletResponse response) {
        try {
            if (exportCount.incrementAndGet() > 1) {
                throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
            }

            String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
            String decryptedable=new String(Base64.getUrlDecoder().decode(encryptedable.getBytes()));
            DisabledExportRequest disabledExportRequest =JSON.parseObject(decryptedable, DisabledExportRequest.class);
            PointsTradeExportRequest pointsTradeExportRequest = JSON.parseObject(decrypted, PointsTradeExportRequest.class);
            DisabledDTO disabledDTO=KsBeanUtil.convert(disabledExportRequest,DisabledDTO.class);

            Operator operator = commonUtil.getOperator();
            log.debug(String.format("/points/trade/export/params, employeeId=%s", operator.getUserId()));

            String adminId = operator.getAdminId();
            if (StringUtils.isNotEmpty(adminId)) {
                pointsTradeExportRequest.setSupplierId(Long.valueOf(adminId));
            }

            //设定状态条件逻辑,已审核状态需筛选出已审核与部分发货
            pointsTradeExportRequest.makeAllAuditFlow();
            PointsTradeQueryDTO pointsTradeQueryDTO = KsBeanUtil.convert(pointsTradeExportRequest, PointsTradeQueryDTO.class);
            List<PointsTradeVO> pointsTrades = pointsTradeQueryProvider.listPointsTradeExport(PointsTradeListExportRequest.builder().
                    pointsTradeQueryDTO(pointsTradeQueryDTO).build()).getContext().getPointsTradeVOList();
            //按下单时间降序排列
            Comparator<PointsTradeVO> c = Comparator.comparing(a -> a.getTradeState().getCreateTime());
            pointsTrades = pointsTrades.stream().sorted(
                    c.reversed()
            ).collect(Collectors.toList());
            String headerKey = "Content-Disposition";
            LocalDateTime dateTime = LocalDateTime.now();
            String fileName = String.format("批量导出积分订单_%s.xls", dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
            try {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("/points/trade/export/params, fileName={},", fileName, e);
            }
            String headerValue = String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName);
            response.setHeader(headerKey, headerValue);
            try {
                List<PointsTradeVO> pointsTradesNew = new ArrayList<>();
                if(disabledDTO.getDisabled().equals("true")){
                    //只导出子订单
                    List<String> parentIdList = pointsTrades.stream().map(PointsTradeVO::getId).collect(Collectors.toList());
                    List<TradeVO> tradeVOList=providerTradeProvider.findByParentIdList(FindProviderTradeRequest.builder().parentId(parentIdList).build()).getContext().getTradeVOList();
                    pointsTrades = KsBeanUtil.convert(tradeVOList,PointsTradeVO.class);
                }

                pointsTrades.forEach(tradeVO -> {
                    List<TradeItemVO> tradeItems = tradeVO.getTradeItems();
                    tradeItems.forEach(tradeItemVO -> {
                        PointsTradeVO tradeVONew = new PointsTradeVO();
                        KsBeanUtil.copyProperties(tradeVO,tradeVONew);
                        List<TradeItemVO> tradeItemsNew = new ArrayList<TradeItemVO>();
                        tradeItemsNew.add(tradeItemVO);
                        tradeVONew.setTradeItems(tradeItemsNew);
                        pointsTradesNew.add(tradeVONew);
                    });
                });

                pointsTradeExportService.export(pointsTradesNew, response.getOutputStream(),
                        Platform.PLATFORM.equals(operator.getPlatform()));
                response.flushBuffer();
            } catch (IOException e) {
                throw new SbcRuntimeException(e);
            }
        } catch (Exception e) {
            log.error("/points/trade/export/params error: ", e);
            throw new SbcRuntimeException(SiteResultCode.ERROR_000001);
        } finally {
            exportCount.set(0);
        }
    }

    /**
     * @return org.springframework.http.ResponseEntity<com.wanmi.sbc.common.base.BaseResponse>
     * @Author lvzhenwei
     * @Description 修改卖家备注
     * @Date 14:27 2019/5/22
     * @Param [tid, tradeRemedyRequest]
     **/
    @ApiOperation(value = "修改卖家备注")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "积分订单id", required = true)
    @RequestMapping(value = "/remark/{tid}", method = RequestMethod.POST)
    @GlobalTransactional
    public ResponseEntity<BaseResponse> sellerRemark(@PathVariable String tid, @RequestBody TradeRemedyDTO
            tradeRemedyRequest) {

        PointsTradeRemedySellerRemarkRequest pointsTradeRemedySellerRemarkRequest = PointsTradeRemedySellerRemarkRequest.builder()
                .sellerRemark(tradeRemedyRequest.getSellerRemark())
                .tid(tid)
                .operator(commonUtil.getOperator())
                .build();

        pointsTradeProvider.remedySellerRemark(pointsTradeRemedySellerRemarkRequest);

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }
}
