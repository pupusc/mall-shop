package com.wanmi.sbc.linkedmall.order;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.linkedmall.model.v20180116.*;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.linkedmall.api.request.order.*;
import com.wanmi.sbc.linkedmall.api.response.order.SbcCreateOrderAndPayResponse;
import com.wanmi.sbc.linkedmall.util.LinkedMallUtil;
import com.wanmi.sbc.setting.bean.vo.CompanyInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class LinkedMallOrderService {

    @Autowired
    private IAcsClient iAcsClient;

    @Autowired
    private LinkedMallUtil linkedMallUtil;

    /**
     * 对接 linkedMall 渲染订单 接口
     * @param sbcRenderOrderRequest
     * @return code=SUCCESS 获取渲染信息成功
     */
    public RenderOrderResponse initRenderOrder(SbcRenderOrderRequest sbcRenderOrderRequest){
        RenderOrderRequest request = new RenderOrderRequest();
        request.setBizId(linkedMallUtil.getLinkedMallBizId());
        request.setBizUid(sbcRenderOrderRequest.getBizUid());
        request.setItemLists(sbcRenderOrderRequest.getLmGoodsItems());

        // 收货地址
        request.setDeliveryAddress(sbcRenderOrderRequest.buildDeliveryAddress());

        request.setThirdPartyUserId(sbcRenderOrderRequest.getBizUid());
        request.setAccountType(LinkedMallUtil.ACCOUNT_TYPE);

        RenderOrderResponse acsResponse = null;
        try {
            acsResponse = iAcsClient.getAcsResponse(request);
            log.info("LinkedMall企业商城API：renderOrder渲染订单，请求参数：{}，请求标识：{}，返回码：{}，返回信息：{}", JSON.toJSONString(request),
                    acsResponse.getRequestId(), acsResponse.getCode(), JSON.toJSONString(acsResponse));
        } catch (ClientException e) {
            log.error("LinkedMall企业商城API: renderOrder渲染订单", e);
            throw new SbcRuntimeException(CommonErrorCode.THIRD_SERVICE_ERROR);
        }

        if (!StringUtils.equals(LinkedMallUtil.SUCCESS_CODE, acsResponse.getCode())) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{acsResponse.getMessage()});
        }

        if(acsResponse.getModel() == null){
            throw new SbcRuntimeException(CommonErrorCode.THIRD_SERVICE_ERROR);
        }
        return acsResponse;
    }

    /**
     * 对接 linkedMall 下单并支付 接口
     * @param sbcCreateOrderAndPayRequest
     * @return code=SUCCESS 下单并支付成功
     */
    public CreateOrderResponse createOrderAndPay(SbcCreateOrderAndPayRequest sbcCreateOrderAndPayRequest){
        CreateOrderRequest request = new CreateOrderRequest();
        request.setBizId(linkedMallUtil.getLinkedMallBizId());
        request.setBizUid(sbcCreateOrderAndPayRequest.getBizUid());
        request.setOutTradeId(sbcCreateOrderAndPayRequest.getOutTradeId());
        request.setItemLists(sbcCreateOrderAndPayRequest.getLmGoodsItems());
        // 收货地址
        request.setDeliveryAddress(sbcCreateOrderAndPayRequest.buildDeliveryAddress());

        request.setThirdPartyUserId(sbcCreateOrderAndPayRequest.getBizUid());
        request.setAccountType(LinkedMallUtil.ACCOUNT_TYPE);

        CreateOrderResponse acsResponse = null;
        try {
            acsResponse = iAcsClient.getAcsResponse(request);
            log.info("LinkedMall企业商城API：createOrder下单并支付，请求参数：{}请求标识：{}，返回码：{}，返回信息：{}", JSON.toJSONString(request),
                    acsResponse.getRequestId(), acsResponse.getCode(), JSON.toJSONString(acsResponse));
        } catch (ClientException e) {
            log.error("LinkedMall企业商城API: createOrder下单并支付", e);
            throw new SbcRuntimeException(CommonErrorCode.THIRD_SERVICE_ERROR);
        }

        if (!StringUtils.equals(LinkedMallUtil.SUCCESS_CODE, acsResponse.getCode())) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{acsResponse.getMessage()});
        }
        return acsResponse;
    }

    /**
     * 对接 linkedMall 只下单不支付 接口
     * @param sbcCreateOrderRequest
     * @return code=SUCCESS 下单成功
     */
    public CreateOrderV2Response createOrder(SbcCreateOrderRequest sbcCreateOrderRequest){
        CreateOrderV2Request request = new CreateOrderV2Request();
        request.setBizId(linkedMallUtil.getLinkedMallBizId());
        request.setBizUid(sbcCreateOrderRequest.getBizUid());
        request.setOutTradeId(sbcCreateOrderRequest.getOutTradeId());
        request.setItemLists(sbcCreateOrderRequest.getLmGoodsItems());
        // 收货地址
        request.setDeliveryAddress(sbcCreateOrderRequest.buildDeliveryAddress());
        // 订单失效时间
        request.setOrderExpireTime(sbcCreateOrderRequest.getOrderExpireTime());

        request.setThirdPartyUserId(sbcCreateOrderRequest.getBizUid());
        request.setAccountType(LinkedMallUtil.ACCOUNT_TYPE);

        CreateOrderV2Response acsResponse = null;
        try {
            acsResponse = iAcsClient.getAcsResponse(request);
            log.info("LinkedMall企业商城API：createOrderV2只下单不支付，请求参数：{}，请求标识：{}，返回码：{}，返回信息：{}", JSON.toJSONString(request),
                    acsResponse.getRequestId(), acsResponse.getCode(), JSON.toJSONString(acsResponse));
        } catch (ClientException e) {
            log.error("LinkedMall企业商城API: createOrderV2只下单不支付", e);
            throw new SbcRuntimeException(CommonErrorCode.THIRD_SERVICE_ERROR);
        }

        if (!StringUtils.equals(LinkedMallUtil.SUCCESS_CODE, acsResponse.getCode())) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{acsResponse.getMessage()});
        }

        if(acsResponse.getModel() == null){
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"数据为空"});
        }
        return acsResponse;
    }

    /**
     * 对接 linkedMall 订单支付 接口，只针对 已下单未支付 订单
     * @param sbcPayOrderRequest
     * @return code=SUCCESS 支付成功
     */
    public EnableOrderResponse payOrder(SbcPayOrderRequest sbcPayOrderRequest){
        EnableOrderRequest request = new EnableOrderRequest();
        request.setBizId(linkedMallUtil.getLinkedMallBizId());
        request.setBizUid(sbcPayOrderRequest.getBizUid());
        request.setLmOrderId(sbcPayOrderRequest.getLmOrderId());
        request.setOutTradeId(sbcPayOrderRequest.getOutTradeId());

        request.setThirdPartyUserId(sbcPayOrderRequest.getBizUid());
        request.setAccountType(LinkedMallUtil.ACCOUNT_TYPE);

        EnableOrderResponse acsResponse = null;
        try {
            acsResponse = iAcsClient.getAcsResponse(request);
            log.info("LinkedMall企业商城API：enableOrder订单支付，请求参数：{}，请求标识：{}，返回码：{}，返回信息：{}", JSON.toJSONString(request),
                    acsResponse.getRequestId(), acsResponse.getCode(), JSON.toJSONString(acsResponse));
        } catch (ClientException e) {
            log.error("LinkedMall企业商城API: enableOrder订单支付", e);
            throw new SbcRuntimeException(CommonErrorCode.THIRD_SERVICE_ERROR);
        }
        if (!StringUtils.equals(LinkedMallUtil.SUCCESS_CODE, acsResponse.getCode())) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{acsResponse.getMessage()});
        }
        if(acsResponse.getModel() == null){
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"数据为空"});
        }
        return acsResponse;
    }

    /**
     * 对接 linkedMall 取消订单 接口，只针对 已下单未支付 订单
     * @param sbcCancelOrderRequest
     * @return code=0000 取消成功
     */
    public CancelOrderResponse cancelOrder(SbcCancelOrderRequest sbcCancelOrderRequest){
        CancelOrderRequest request = new CancelOrderRequest();
        request.setBizId(linkedMallUtil.getLinkedMallBizId());
        request.setBizUid(sbcCancelOrderRequest.getBizUid());
        request.setLmOrderId(sbcCancelOrderRequest.getLmOrderId());

        request.setThirdPartyUserId(sbcCancelOrderRequest.getBizUid());
        request.setAccountType(LinkedMallUtil.ACCOUNT_TYPE);

        CancelOrderResponse acsResponse = null;
        try {
            acsResponse = iAcsClient.getAcsResponse(request);
            log.info("LinkedMall企业商城API：cancelOrder取消订单，请求参数：{}，请求标识：{}，返回码：{}，返回信息：{}", JSON.toJSONString(request),
                     acsResponse.getRequestId(), acsResponse.getCode(), JSON.toJSONString(acsResponse));
        } catch (ClientException e) {
            log.error("LinkedMall企业商城API: cancelOrder取消订单", e);
            throw new SbcRuntimeException(CommonErrorCode.THIRD_SERVICE_ERROR);
        }
        if (!StringUtils.equals(LinkedMallUtil.SUCCESS_CODE, acsResponse.getCode())) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{acsResponse.getMessage()});
        }
        return acsResponse;
    }

    /**
     * 对接 linkedMall 订单物流查询 接口
     * @param sbcLogisticsQueryRequest
     * @return linkedMall 订单id 对应商品物流信息  code=SUCCESS 查询成功
     */
    public List<QueryLogisticsResponse.DataItem> queryLogistics(SbcLogisticsQueryRequest sbcLogisticsQueryRequest){
        QueryLogisticsRequest request = new QueryLogisticsRequest();
        request.setBizId(linkedMallUtil.getLinkedMallBizId());
        request.setBizUid(sbcLogisticsQueryRequest.getBizUid());
        request.setLmOrderId(sbcLogisticsQueryRequest.getLmOrderId());

        request.setThirdPartyUserId(sbcLogisticsQueryRequest.getBizUid());
        request.setAccountType(LinkedMallUtil.ACCOUNT_TYPE);

        QueryLogisticsResponse acsResponse = null;
        try {
            acsResponse = iAcsClient.getAcsResponse(request);
            log.info("LinkedMall企业商城API：queryLogistics订单物流查询，请求参数：{}，请求标识：{}，返回码：{}，返回信息：{}", JSON.toJSONString(request),
                    acsResponse.getRequestId(), acsResponse.getCode(), JSON.toJSONString(acsResponse));
        } catch (ClientException e) {
            log.error("LinkedMall企业商城API: queryLogistics订单物流查询", e);
            throw new SbcRuntimeException(CommonErrorCode.THIRD_SERVICE_ERROR);
        }
        if (!StringUtils.equals(LinkedMallUtil.SUCCESS_CODE, acsResponse.getCode())) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{acsResponse.getMessage()});
        }
        List<QueryLogisticsResponse.DataItem> dataItemList = acsResponse.getData();

        if(CollectionUtils.isEmpty(acsResponse.getData())){
            throw new SbcRuntimeException(CommonErrorCode.THIRD_SERVICE_ERROR);
        }

        return dataItemList;
    }

    /**
     * 对接 linkedMall 确认收货 接口
     * @param sbcConfirmDisburseRequest
     * @return code=SUCCESS 确认收货成功
     */
    public ConfirmDisburseResponse confirmDisburse(SbcConfirmDisburseRequest sbcConfirmDisburseRequest){
        ConfirmDisburseRequest request = new ConfirmDisburseRequest();
        request.setBizId(linkedMallUtil.getLinkedMallBizId());
        request.setBizUid(sbcConfirmDisburseRequest.getBizUid());
        request.setLmOrderId(sbcConfirmDisburseRequest.getLmOrderId());

        request.setThirdPartyUserId(sbcConfirmDisburseRequest.getBizUid());
        request.setAccountType(LinkedMallUtil.ACCOUNT_TYPE);

        ConfirmDisburseResponse acsResponse = null;
        try {
            acsResponse = iAcsClient.getAcsResponse(request);
            log.info("LinkedMall企业商城API：confirmDisburse确认收货，请求参数：{}，请求标识：{}，返回码：{}，返回信息：{}", JSON.toJSONString(request),
                    acsResponse.getRequestId(), acsResponse.getCode(), JSON.toJSONString(acsResponse));
        } catch (ClientException e) {
            log.error("LinkedMall企业商城API: confirmDisburse确认收货", e);
            throw new SbcRuntimeException(CommonErrorCode.THIRD_SERVICE_ERROR);
        }
        if (!StringUtils.equals(LinkedMallUtil.SUCCESS_CODE, acsResponse.getCode())) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{acsResponse.getMessage()});
        }
        return acsResponse;
    }

    /**
     * 对接 linkedMall 查询订单详情/列表 接口
     * @param
     * @return code=SUCCESS 查询成功
     */
    public QueryOrderListResponse queryOrder(SbcOrderListQueryRequest sbcOrderListQueryRequest){
        QueryOrderListRequest request = new QueryOrderListRequest();
        request.setBizId(linkedMallUtil.getLinkedMallBizId());
        request.setBizUid(sbcOrderListQueryRequest.getBizUid());
        request.setFilterOption(sbcOrderListQueryRequest.buildFilterOption());
        request.setPageNumber(sbcOrderListQueryRequest.getPageNum());
        request.setPageSize(sbcOrderListQueryRequest.getPageSize());

        request.setThirdPartyUserId(sbcOrderListQueryRequest.getBizUid());
        request.setAccountType(LinkedMallUtil.ACCOUNT_TYPE);

        QueryOrderListResponse acsResponse = null;
        try {
            acsResponse = iAcsClient.getAcsResponse(request);
            log.info("LinkedMall企业商城API：queryOrderList查询订单详情/列表，请求参数：{}，请求标识：{}，返回码：{}，返回信息：{}", JSON.toJSONString(request),
                    acsResponse.getRequestId(), acsResponse.getCode(), JSON.toJSONString(acsResponse));
        } catch (ClientException e) {
            log.error("LinkedMall企业商城API: queryOrderList查询订单详情/列表", e);
            throw new SbcRuntimeException(CommonErrorCode.THIRD_SERVICE_ERROR);
        }
        if (!StringUtils.equals(LinkedMallUtil.SUCCESS_CODE, acsResponse.getCode())) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{acsResponse.getMessage()});
        }

        if(Boolean.TRUE.equals(sbcOrderListQueryRequest.getAllFlag())) {
            List<QueryOrderListResponse.LmOrderListItem> list = acsResponse.getLmOrderList();
            long size = sbcOrderListQueryRequest.getPageSize();
            long total = sbcOrderListQueryRequest.getLmOrderList().size();
            long count = 1;
            long m = total % size;
            if (m > 0) {
                count = total / size + 1;
            } else {
                count = total / size;
            }
            for (long i = 2; i <= count; i++) {
                request.setPageNumber(i);
                QueryOrderListResponse acsResponse1 = null;
                try {
                    acsResponse1 = iAcsClient.getAcsResponse(request);
                    log.info("LinkedMall企业商城API：queryOrderList查询订单详情/列表，请求参数：{}，请求标识：{}，返回码：{}，返回信息：{}", JSON.toJSONString(request),
                            acsResponse1.getRequestId(), acsResponse1.getCode(), JSON.toJSONString(acsResponse1));
                } catch (ClientException e) {
                    log.error("LinkedMall企业商城API: queryOrderList查询订单详情/列表", e);
                    throw new SbcRuntimeException(CommonErrorCode.THIRD_SERVICE_ERROR);
                }
                if (!StringUtils.equals(LinkedMallUtil.SUCCESS_CODE, acsResponse1.getCode())) {
                    throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{acsResponse.getMessage()});
                }
                if(CollectionUtils.isNotEmpty(acsResponse1.getLmOrderList())) {
                    list.addAll(acsResponse1.getLmOrderList());
                }
            }
            acsResponse.setLmOrderList(list);
        }
        return acsResponse;
    }

}
