//package com.wanmi.sbc.order.service;
//
//import com.alibaba.fastjson.JSON;
//import com.google.common.collect.Lists;
//import com.wanmi.sbc.common.base.BaseResponse;
//import com.wanmi.sbc.common.base.MicroServicePage;
//import com.wanmi.sbc.common.exception.SbcRuntimeException;
//import com.wanmi.sbc.common.util.KsBeanUtil;
//import com.wanmi.sbc.log.model.entity.YzLog;
//import com.wanmi.sbc.log.service.YzLogService;
//import com.wanmi.sbc.order.api.provider.yzorder.YzOrderProvider;
//import com.wanmi.sbc.order.api.request.yzorder.YzOrderAddBatchRequest;
//import com.wanmi.sbc.order.api.request.yzorder.YzOrderPageQueryRequest;
//import com.wanmi.sbc.order.api.response.yzorder.YzOrderPageResponse;
//import com.wanmi.sbc.order.bean.dto.yzorder.*;
//import com.wanmi.sbc.order.bean.dto.yzorder.deliver.DistOrderItemOpenDTO;
//import com.wanmi.sbc.order.bean.dto.yzorder.deliver.MultiPeriodOrderDeliverDTO;
//import com.wanmi.sbc.order.bean.dto.yzorder.deliver.YzOrderDeliverDTO;
//import com.wanmi.sbc.order.request.YzOrderConvertRequest;
//import com.wanmi.sbc.order.request.YzOrderMigrateRequest;
//import com.wanmi.sbc.order.request.YzOrderUpdateRequest;
//import com.wanmi.sbc.yz.YzTokenService;
//import com.youzan.cloud.open.sdk.common.exception.SDKException;
//import com.youzan.cloud.open.sdk.core.client.auth.Token;
//import com.youzan.cloud.open.sdk.core.client.core.YouZanClient;
//import com.youzan.cloud.open.sdk.gen.v1_0_0.api.YouzanTradeDcQueryMultiperiodQuerydeliveryrecords;
//import com.youzan.cloud.open.sdk.gen.v1_0_0.api.YouzanTradeDcQueryMultiperiodQuerylatestplan;
//import com.youzan.cloud.open.sdk.gen.v1_0_0.api.YouzanTradeOpenPcOrderPromotion;
//import com.youzan.cloud.open.sdk.gen.v1_0_0.model.*;
//import com.youzan.cloud.open.sdk.gen.v1_0_1.api.YouzanTradeDcQueryQuerybyorderno;
//import com.youzan.cloud.open.sdk.gen.v1_0_1.model.YouzanTradeDcQueryQuerybyordernoParams;
//import com.youzan.cloud.open.sdk.gen.v1_0_1.model.YouzanTradeDcQueryQuerybyordernoResult;
//import com.youzan.cloud.open.sdk.gen.v2_0_0.api.YouzanTradeDcQueryMultiperioddetail;
//import com.youzan.cloud.open.sdk.gen.v2_0_0.model.YouzanTradeDcQueryMultiperioddetailParams;
//import com.youzan.cloud.open.sdk.gen.v2_0_0.model.YouzanTradeDcQueryMultiperioddetailResult;
//import com.youzan.cloud.open.sdk.gen.v4_0_0.api.YouzanTradeGet;
//import com.youzan.cloud.open.sdk.gen.v4_0_0.model.YouzanTradeGetParams;
//import com.youzan.cloud.open.sdk.gen.v4_0_0.model.YouzanTradeGetResult;
//import com.youzan.cloud.open.sdk.gen.v4_0_1.api.YouzanTradesSoldGet;
//import com.youzan.cloud.open.sdk.gen.v4_0_1.model.YouzanTradesSoldGetParams;
//import com.youzan.cloud.open.sdk.gen.v4_0_1.model.YouzanTradesSoldGetResult;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.math.NumberUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//@Slf4j
//public class YzOrderService {
//
//    @Autowired
//    private YouZanClient yzClient;
//
//    @Autowired
//    private YzTokenService yzTokenService;
//
//    @Autowired
//    private YzOrderProvider yzOrderProvider;
//
//    private Integer pageSize = 100;
//
//    @Autowired
//    private YzLogService yzLogService;
//
//    public BaseResponse migrateOrder(YzOrderMigrateRequest request) {
//        Integer maxPageNo = 100;
//        Integer count  = 0;
//        do{
//            long start = System.currentTimeMillis();
//            //是否重试
//            Boolean retryFlag = Boolean.FALSE;
//            YouzanTradesSoldGetResult.YouzanTradesSoldGetResultData resultData = null;
//            List<YzOrderDTO> yzOrderDTOS = new ArrayList<>();
//            try {
//                try {
//                    Date createAtStart = null;
//                    Date createAtEnd = null;
//                    if(request.getCreateAtStart() != null && request.getCreateAtEnd() != null) {
//                        createAtStart = Date.from(request.getCreateAtStart().atZone(ZoneId.systemDefault()).toInstant());
//                        createAtEnd = Date.from(request.getCreateAtEnd().atZone(ZoneId.systemDefault()).toInstant());
//                    }
//
//                    resultData = searchOrderList(request.getPageNo(),request.getStatus(), createAtStart, createAtEnd, request.getTid());
//
//                    for (YouzanTradesSoldGetResult.YouzanTradesSoldGetResultFullorderinfolist info: resultData.getFullOrderInfoList()) {
//                        YzOrderDTO orderDTO = new YzOrderDTO();
//                        FullOrderInfoDTO fullOrderInfoDTO = KsBeanUtil.convert(info.getFullOrderInfo(), FullOrderInfoDTO.class);
//                        orderDTO.setFull_order_info(fullOrderInfoDTO);;
//                        orderDTO.setId(orderDTO.getFull_order_info().getOrder_info().getTid());
//
//                        //组装订单详情数据
//                        getOrderDetail(orderDTO);
//                        //组装订单优惠数据
//                        getOrderPromotion(orderDTO);
//                        //发货记录
//                        Long express_type = orderDTO.getFull_order_info().getOrder_info().getExpress_type();
//                        //express_type 0:快递发货; 1:到店自提;
//                        if(express_type == 0L || express_type == 1L) {
//                            getOrderDeliver(orderDTO);
//                        }
//                        //周期购
//                        if(orderDTO.getFull_order_info().getOrders().get(0).getItem_type() == 24) {
//                            //周期购信息
//                            getMultiPeriodDetail(orderDTO);
//                            //最近一次发货计划
//                            getMultiPeriodLatestPlan(orderDTO);
//                            //周期购发货记录
//                            //getMultiPeriodOrderDeliver(orderDTO);
//                        }
//
//                        yzOrderDTOS.add(orderDTO);
//                    }
//                } catch (SDKException e) {
//                    log.error("同步订单列表报错，createAtStart:{}, createAtEnd:{},pageNo:{},status:{}",
//                            request.getCreateAtStart(), request.getCreateAtEnd(),request.getPageNo(),request.getStatus());
//                    log.error("报错信息：code:{},message:{}",e.getCode(),e.getMessage());
//                    if(e.getCode()==4203) {
//                        //token校验不通过，删除重试
//                        yzTokenService.removeToken();
//                        retryFlag = Boolean.TRUE;
//                        yzOrderDTOS.clear();
//                    }else if(e.getCode() == 102 && count < 3){
//                        //超时重试
//                        count ++;
//                        retryFlag = Boolean.TRUE;
//                        yzOrderDTOS.clear();
//                    } else {
//                        throw new SbcRuntimeException(e.getCode(), "", e.getMessage());
//                    }
//                }
//
//                //批量保存订单数据
//                if(CollectionUtils.isNotEmpty(yzOrderDTOS)) {
//                    yzOrderProvider.addBatch(YzOrderAddBatchRequest.builder().yzOrders(yzOrderDTOS).updateFlag(request.getUpdateFlag()).build());
//                }
//
//                Long total = resultData.getTotalResults();
//                Long totalPage = total % pageSize == 0 ? total/pageSize : total/pageSize + 1;
//                maxPageNo = totalPage > 100 ? 100 : totalPage.intValue();
//                long end = System.currentTimeMillis();
//                log.info("当前查询条件：{},当前页数据量{},当前条件已处理页数{},共{}条,当前页耗费时间:{}",
//                        JSON.toJSONString(request),
//                        yzOrderDTOS.size(),
//                        request.getPageNo(),
//                        total,
//                        (end-start));
//
//            } catch (Exception e) {
//                YzLog yzLog = YzLog.builder().mod("order").condition(JSON.toJSONString(request)).exception(JSON.toJSONString(e)).createTime(LocalDateTime.now()).build();
//                yzLogService.addLog(yzLog);
//            }
//
//            //不重试的情况下才更改页码
//            if(Boolean.FALSE.equals(retryFlag)) {
//                //如果是在100页以内，继续遍历下一页
//                if(request.getPageNo() <= maxPageNo) {
//                    request.setPageNo(request.getPageNo() + 1);
//                    count = 0;
//                }
//
//                if( request.getPageNo() > 100) {
//                    //如果超过100页，继续下一个100页，取最后一条记录的创建时间作为条件
//                    count = 0;
//                    request.setPageNo(1);
//                    List<YouzanTradesSoldGetResult.YouzanTradesSoldGetResultFullorderinfolist> list = resultData.getFullOrderInfoList();
//                    if(request.getCreateAtEnd() != null) {
//                        Date createdTime = list.get(list.size() - 1).getFullOrderInfo().getOrderInfo().getCreated();
//                        request.setCreateAtEnd(LocalDateTime.ofInstant(createdTime.toInstant(), ZoneId.systemDefault()).plusSeconds(1L));
//                    }
//                }
//            }
//
//            //是否只查询当前页
//            if(Boolean.TRUE.equals(request.getOnlyOnePage())) {
//                break;
//            }
//        }while(request.getPageNo() <= maxPageNo);
//
//        return BaseResponse.SUCCESSFUL();
//    }
//
//    /**
//     * 查询订单列表
//     * @param pageNo
//     * @param status
//     * @return
//     * @throws SDKException
//     */
//    public YouzanTradesSoldGetResult.YouzanTradesSoldGetResultData searchOrderList(Integer pageNo, String status, Date createAtStart, Date createAtEnd, String tid) throws SDKException {
//        String accessToken = yzTokenService.getToken();
//        Token token = new Token(accessToken);
//
//        YouzanTradesSoldGet youzanTradesSoldGet = new YouzanTradesSoldGet();
//        //创建参数对象,并设置参数
//        YouzanTradesSoldGetParams youzanTradesSoldGetParams = new YouzanTradesSoldGetParams();
//        youzanTradesSoldGetParams.setStartCreated(createAtStart);
//        youzanTradesSoldGetParams.setEndCreated(createAtEnd);
//        youzanTradesSoldGetParams.setStatus(status);
//        youzanTradesSoldGetParams.setPageNo(pageNo);
//        youzanTradesSoldGetParams.setPageSize(pageSize);
//        youzanTradesSoldGetParams.setTid(tid);
//        youzanTradesSoldGet.setAPIParams(youzanTradesSoldGetParams);
//
//        YouzanTradesSoldGetResult result = yzClient.invoke(youzanTradesSoldGet, token, YouzanTradesSoldGetResult.class);
//
//        if(result.getCode() != 200) {
//            throw new SDKException(result.getCode(), result.getMessage());
//        }
//        return result.getData();
//
//    }
//
//    /**
//     * 获取详情信息
//     * @param order
//     */
//    private void getOrderDetail(YzOrderDTO order) throws SDKException {
//        String accessToken = yzTokenService.getToken();
//        Token token = new Token(accessToken);
//
//        YouzanTradeGet youzanTradeGet = new YouzanTradeGet();
//        //创建参数对象,并设置参数
//        YouzanTradeGetParams youzanTradeGetParams = new YouzanTradeGetParams();
//        youzanTradeGetParams.setTid(order.getId());
//        youzanTradeGet.setAPIParams(youzanTradeGetParams);
//
//        YouzanTradeGetResult result = yzClient.invoke(youzanTradeGet, token, YouzanTradeGetResult.class);
//
//        if(result.getCode() != 200) {
//            throw new SDKException(result.getCode(), result.getMessage());
//        }
//        mergeOrderDetail(result.getData(), order);
//    }
//
//    /**
//     * 获取优惠信息
//     * @param order
//     */
//    private void getOrderPromotion(YzOrderDTO order) throws SDKException {
//        String accessToken = yzTokenService.getToken();
//        Token token = new Token(accessToken);
//
//        YouzanTradeOpenPcOrderPromotion youzanTradeOpenPcOrderPromotion = new YouzanTradeOpenPcOrderPromotion();
//        //创建参数对象,并设置参数
//        YouzanTradeOpenPcOrderPromotionParams youzanTradeOpenPcOrderPromotionParams = new YouzanTradeOpenPcOrderPromotionParams();
//        youzanTradeOpenPcOrderPromotionParams.setPostagePromotion(true);
//        youzanTradeOpenPcOrderPromotionParams.setGoodsLevelPromotion(true);
//        youzanTradeOpenPcOrderPromotionParams.setOrderSharePromotion(true);
//        youzanTradeOpenPcOrderPromotionParams.setTid(order.getId());
//        youzanTradeOpenPcOrderPromotion.setAPIParams(youzanTradeOpenPcOrderPromotionParams);
//
//        YouzanTradeOpenPcOrderPromotionResult result = yzClient.invoke(youzanTradeOpenPcOrderPromotion, token, YouzanTradeOpenPcOrderPromotionResult.class);
//        if(result.getCode() != 200) {
//            throw new SDKException(result.getCode(), result.getMessage());
//        }
//        mergeOrderPromotion(result.getData(), order);
//    }
//
//    /**
//     * 获取周期购信息
//     */
//    private void getMultiPeriodDetail(YzOrderDTO order) throws SDKException {
//        String accessToken = yzTokenService.getToken();
//        Token token = new Token(accessToken);
//
//        YouzanTradeDcQueryMultiperioddetail youzanTradeDcQueryMultiperioddetail = new YouzanTradeDcQueryMultiperioddetail();
//        //创建参数对象,并设置参数
//        YouzanTradeDcQueryMultiperioddetailParams youzanTradeDcQueryMultiperioddetailParams = new YouzanTradeDcQueryMultiperioddetailParams();
//        youzanTradeDcQueryMultiperioddetailParams.setTid(order.getId());
//        youzanTradeDcQueryMultiperioddetailParams.setOid(order.getFull_order_info().getOrders().get(0).getOid());
//        youzanTradeDcQueryMultiperioddetail.setAPIParams(youzanTradeDcQueryMultiperioddetailParams);
//
//        YouzanTradeDcQueryMultiperioddetailResult result = yzClient.invoke(youzanTradeDcQueryMultiperioddetail, token, YouzanTradeDcQueryMultiperioddetailResult.class);
//
//        if(result.getCode() != 200) {
//            throw new SDKException(result.getCode(), result.getMessage());
//        }
//
//        MultiPeriodDetailDTO multiPeriodDetailDTO = KsBeanUtil.convert(result.getData(), MultiPeriodDetailDTO.class);
//        order.getFull_order_info().setMultiPeriodDetail(multiPeriodDetailDTO);
//    }
//
//    /**
//     * 获取周期购订单最近一次发货计划
//     * @param order
//     */
//    private void getMultiPeriodLatestPlan(YzOrderDTO order) throws SDKException {
//        String accessToken = yzTokenService.getToken();
//        Token token = new Token(accessToken);
//
//        YouzanTradeDcQueryMultiperiodQuerylatestplan youzanTradeDcQueryMultiperiodQuerylatestplan = new YouzanTradeDcQueryMultiperiodQuerylatestplan();
//        //创建参数对象,并设置参数
//        YouzanTradeDcQueryMultiperiodQuerylatestplanParams youzanTradeDcQueryMultiperiodQuerylatestplanParams = new YouzanTradeDcQueryMultiperiodQuerylatestplanParams();
//        youzanTradeDcQueryMultiperiodQuerylatestplanParams.setTid(order.getId());
//        youzanTradeDcQueryMultiperiodQuerylatestplanParams.setOid(order.getFull_order_info().getOrders().get(0).getOid());
//        youzanTradeDcQueryMultiperiodQuerylatestplan.setAPIParams(youzanTradeDcQueryMultiperiodQuerylatestplanParams);
//
//        YouzanTradeDcQueryMultiperiodQuerylatestplanResult result = yzClient.invoke(youzanTradeDcQueryMultiperiodQuerylatestplan, token, YouzanTradeDcQueryMultiperiodQuerylatestplanResult.class);
//
//        if(result.getCode() != 200) {
//            throw new SDKException(result.getCode(), result.getMessage());
//        }
//
//        MultiPeriodLatestPlanDTO latestPlanDTO = KsBeanUtil.convert(result.getData(), MultiPeriodLatestPlanDTO.class);
//        order.getFull_order_info().setMultiPeriodLatestPlan(latestPlanDTO);
//    }
//
//    /**
//     * 获取发货记录
//     * @param order
//     */
//    private void getOrderDeliver(YzOrderDTO order) throws SDKException {
//        String accessToken = yzTokenService.getToken();
//        Token token = new Token(accessToken);
//
//        YouzanTradeDcQueryQuerybyorderno youzanTradeDcQueryQuerybyorderno = new YouzanTradeDcQueryQuerybyorderno();
//        //创建参数对象,并设置参数
//        YouzanTradeDcQueryQuerybyordernoParams youzanTradeDcQueryQuerybyordernoParams = new YouzanTradeDcQueryQuerybyordernoParams();
//        youzanTradeDcQueryQuerybyordernoParams.setIncludeCanceledDistOrder(false);
//        youzanTradeDcQueryQuerybyordernoParams.setIncludeOperationLog(false);
//        youzanTradeDcQueryQuerybyordernoParams.setOpenQueryRequestincludeDistOrderAndDetail(true);
//        youzanTradeDcQueryQuerybyordernoParams.setIncludeDistOrder(true);
//        youzanTradeDcQueryQuerybyordernoParams.setIncludeItemDeliveryStatus(true);
//        youzanTradeDcQueryQuerybyordernoParams.setTid(order.getId().toUpperCase());
//        youzanTradeDcQueryQuerybyordernoParams.setIncludeAllDistOrder(false);
//        youzanTradeDcQueryQuerybyorderno.setAPIParams(youzanTradeDcQueryQuerybyordernoParams);
//
//        YouzanTradeDcQueryQuerybyordernoResult result = yzClient.invoke(youzanTradeDcQueryQuerybyorderno, token, YouzanTradeDcQueryQuerybyordernoResult.class);
//
//        if(result.getCode() != 200) {
//            throw new SDKException(result.getCode(), result.getMessage());
//        }
//
//        mergeOrderDeliver(result.getData(), order);
//    }
//
//    /**
//     * 获取周期购订单发货记录
//     * @param order
//     */
//    private void getMultiPeriodOrderDeliver(YzOrderDTO order) throws SDKException {
//        String accessToken = yzTokenService.getToken();
//        Token token = new Token(accessToken);
//
//        YouzanTradeDcQueryMultiperiodQuerydeliveryrecords youzanTradeDcQueryMultiperiodQuerydeliveryrecords = new YouzanTradeDcQueryMultiperiodQuerydeliveryrecords();
//        //创建参数对象,并设置参数
//        YouzanTradeDcQueryMultiperiodQuerydeliveryrecordsParams params = new YouzanTradeDcQueryMultiperiodQuerydeliveryrecordsParams();
//        params.setTid(order.getId());
//        youzanTradeDcQueryMultiperiodQuerydeliveryrecords.setAPIParams(params);
//
//        YouzanTradeDcQueryMultiperiodQuerydeliveryrecordsResult result = yzClient.invoke(youzanTradeDcQueryMultiperiodQuerydeliveryrecords, token, YouzanTradeDcQueryMultiperiodQuerydeliveryrecordsResult.class);
//
//        if(result.getCode() != 200) {
//            throw new SDKException(result.getCode(), result.getMessage());
//        }
//
//        mergeMultiOrderDeliver(result.getData(), order);
//    }
//
//    /**
//     * 组装订单详情中的数据
//     * @param resultData
//     * @param order
//     */
//    private void mergeOrderDetail(YouzanTradeGetResult.YouzanTradeGetResultData resultData, YzOrderDTO order){
//        order.setDelivery_order(KsBeanUtil.convert(resultData.getDeliveryOrder(), DeliveryOrderDTO.class));
//        order.setRefund_order(KsBeanUtil.convert(resultData.getRefundOrder(), RefundOrderDTO.class));
//        order.setOrder_promotion(KsBeanUtil.convert(resultData.getOrderPromotion(), OrderPromotionDTO.class));
//
//        KsBeanUtil.copyProperties(KsBeanUtil.convert(resultData.getFullOrderInfo(), FullOrderInfoDTO.class), order.getFull_order_info());
//
//    }
//
//    /**
//     * 组装订单优惠数据
//     * @param resultData
//     * @param order
//     */
//    private void mergeOrderPromotion(YouzanTradeOpenPcOrderPromotionResult.YouzanTradeOpenPcOrderPromotionResultData resultData, YzOrderDTO order) {
//        OrderPromotionDTO promotionDTO = KsBeanUtil.convert(resultData, OrderPromotionDTO.class);
//        KsBeanUtil.copyProperties(promotionDTO, order.getOrder_promotion());
//    }
//
//    /**
//     * 组装发货信息
//     */
//    private void mergeOrderDeliver(List<YouzanTradeDcQueryQuerybyordernoResult.YouzanTradeDcQueryQuerybyordernoResultData> resultData, YzOrderDTO order){
//        List<YzOrderDeliverDTO> deliverDTOS = resultData.stream().map(data -> KsBeanUtil.convert(data, YzOrderDeliverDTO.class)).collect(Collectors.toList());
//        order.setDelivery_order_detail(deliverDTOS);
//    }
//
//    /**
//     * 组装周期购发货信息
//     */
//    private void mergeMultiOrderDeliver(List<YouzanTradeDcQueryMultiperiodQuerydeliveryrecordsResult.YouzanTradeDcQueryMultiperiodQuerydeliveryrecordsResultData> resultData, YzOrderDTO order){
//        List<MultiPeriodOrderDeliverDTO> deliverDTOS = resultData.stream().map(data -> {
//            MultiPeriodOrderDeliverDTO dto = KsBeanUtil.convert(data, MultiPeriodOrderDeliverDTO.class);
//            data.getMultiPeriodDistInfo().forEach(info -> {
//                List<DistOrderItemOpenDTO> itemList = info.getDistOrder().getDistOrderItemOpens().stream().map(item -> KsBeanUtil.convert(item, DistOrderItemOpenDTO.class)).collect(Collectors.toList());
//                dto.getMulti_period_dist_info().forEach(dtoInfo -> {
//                    if(dtoInfo.getDist_order().getDist_id().equals(info.getDistOrder().getDistId())) {
//                        dtoInfo.getDist_order().setDist_order_items(itemList);
//                    }
//                });
//            });
//            return dto;
//        }).collect(Collectors.toList());
//
//        order.setMulti_period_order_deliver(deliverDTOS);
//    }
//
//    /**
//     * 有赞订单入库
//     * @param request
//     */
//    public void convertType(YzOrderConvertRequest request){
//        YzOrderPageQueryRequest queryRequest = new YzOrderPageQueryRequest();
//        queryRequest.setCreateTimeStart(request.getCreateAtStart());
//        queryRequest.setCreateTimeEnd(request.getCreateAtEnd());
//        if(request.getStatus() != null) {
//            queryRequest.setStatus(Lists.newArrayList(request.getStatus()));
//        }
//        queryRequest.setIds(request.getIds());
//        queryRequest.setPageNum(request.getPageNo());
//        queryRequest.setPageSize(request.getPageSize());
//        queryRequest.setConvertFlag(request.getConvertFlag());
//        yzOrderProvider.convertType(queryRequest);
//    }
//
//
//    /**
//     * 更新订单
//     * @param yzOrderUpdateRequest
//     */
//    public void updateOrder(YzOrderUpdateRequest yzOrderUpdateRequest){
//        String[] orderStatus = {"WAIT_BUYER_PAY","WAIT_SELLER_SEND_GOODS","WAIT_BUYER_CONFIRM_GOODS"};
//        YzOrderPageQueryRequest queryRequest = new YzOrderPageQueryRequest();
//        queryRequest.setStatus(Lists.newArrayList(orderStatus));
//        queryRequest.setCreateTimeStart(yzOrderUpdateRequest.getCreateTimeStart());
//        queryRequest.setCreateTimeEnd(yzOrderUpdateRequest.getCreateTimeEnd());
//        List<String> ids = yzOrderProvider.getYzOrderIdList(queryRequest).getContext().getIds();
//
//        log.info("-------当前查询订单量：{}-----", ids.size());
//
//        ids.forEach(id -> {
//            YzOrderMigrateRequest migrateRequest = new YzOrderMigrateRequest();
//            migrateRequest.setTid(id);
//            migrateRequest.setPageNo(NumberUtils.INTEGER_ONE);
//            migrateRequest.setUpdateFlag(Boolean.TRUE);
//            migrateRequest.setOnlyOnePage(Boolean.TRUE);
//            migrateOrder(migrateRequest);
//        });
//    }
//
//
//}
