package com.wanmi.sbc.returnorder.service;

import com.wanmi.sbc.client.CancelOrderRequest;
import com.wanmi.sbc.client.CancelOrderResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.response.trade.TradeGetByIdResponse;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.ReturnType;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/1/6 9:44 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class BoKuService extends AbstractCRMService{


    @Override
    public BaseResponse interceptorErpDeliverStatus(String returnOrderId, Boolean flag){
        ReturnOrderVO returnOrderVO = super.getReturnOrderVo(returnOrderId);
        //获取订单信息
        TradeGetByIdRequest tradeGetByIdRequest = TradeGetByIdRequest.builder().tid(returnOrderVO.getTid()).build();
        BaseResponse<TradeGetByIdResponse> tradeResponse = tradeQueryProvider.getById(tradeGetByIdRequest);
        TradeVO tradeVO = tradeResponse.getContext().getTradeVO();
        if (CollectionUtils.isEmpty(tradeResponse.getContext().getTradeVO().getTradeVOList())){
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }

        //判断订单中是否包含虚拟商品和电子卡券，防止用户刷商品
        if (flag){
            List<TradeItemVO> tradeItemVOList = tradeVO.getGifts().stream().filter(tradeItemVO ->
                    tradeItemVO.getGoodsType().equals(GoodsType.VIRTUAL_GOODS) || tradeItemVO.getGoodsType()
                            .equals(GoodsType.VIRTUAL_COUPON)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(tradeItemVOList)){
                throw new SbcRuntimeException("K-050319");
            }
        }

        DeliverStatus deliverStatus = tradeVO.getTradeState().getDeliverStatus();

        if (deliverStatus.equals(DeliverStatus.NOT_YET_SHIPPED) || deliverStatus.equals(DeliverStatus.PART_SHIPPED)) {
            //这里获取的实际上是子单
            List<TradeVO> providerTradeVoList = tradeVO.getTradeVOList().stream().filter(p -> p.getId().equals(returnOrderVO.getPtid())).collect(Collectors.toList());
            for (TradeVO providerTradeParam : providerTradeVoList) {
                BaseResponse<CancelOrderResponse> response = bizSupplierClient.cancelOrder(CancelOrderRequest.builder().orderId(providerTradeParam.getDeliveryOrderId()).pid(providerTradeParam.getId()).build());
                if(response == null || response.getContext() == null || !Objects.equals(response.getContext().getStatus(),1)){
                    throw new SbcRuntimeException("K-050143", new Object[]{response !=null && response.getContext() != null && StringUtils.isNotEmpty(response.getContext().getErrorMsg())?response.getContext().getErrorMsg():"订单取消失败"});
                }
            }
        }

        if (returnOrderVO.getReturnType().equals(ReturnType.RETURN)) {
            log.info("博库退货退款请走博库平台");
        }
//            if(!Objects.equals(defaultProviderId,returnOrderVO.getProviderId())){
//                log.info("博库退货退款请走博库平台");
//                return BaseResponse.SUCCESSFUL();
//            }

        return BaseResponse.SUCCESSFUL();
    }

}
