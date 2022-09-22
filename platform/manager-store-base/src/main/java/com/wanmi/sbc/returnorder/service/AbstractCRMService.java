package com.wanmi.sbc.returnorder.service;

import com.wanmi.sbc.client.BizSupplierClient;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.erp.api.provider.GuanyierpProvider;
import com.wanmi.sbc.erp.api.provider.ShopCenterDeliveryProvider;
import com.wanmi.sbc.erp.api.provider.ShopCenterOrderProvider;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderByIdRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.response.trade.TradeGetByIdResponse;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/1/6 9:46 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public abstract class AbstractCRMService {

//    @Autowired
//    protected GuanyierpProvider guanyierpProvider;

    @Autowired
    protected ReturnOrderQueryProvider returnOrderQueryProvider;

    @Autowired
    protected TradeQueryProvider tradeQueryProvider;

    @Autowired
    protected BizSupplierClient bizSupplierClient;

    @Autowired
    protected ShopCenterDeliveryProvider shopCenterDeliveryProvider;

    @Autowired
    protected ShopCenterOrderProvider shopCenterOrderProvider;


    /**
     * 管易云
     */
    @Value("${default.providerId}")
    protected String defaultProviderId;

    /**
     * 获取退单信息
     * @param returnOrderId
     * @return
     */
    protected ReturnOrderVO getReturnOrderVo(String returnOrderId) {
        //获取退单信息
        return returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder().rid(returnOrderId).build()).getContext();
    }

    /**
     * 获取订单和子订单列表
     * @param orderId
     * @param flag
     * @return
     */
    protected TradeGetByIdResponse getTradeAndProviderTrade(String orderId, boolean flag) {
        //获取订单信息
        TradeGetByIdRequest tradeGetByIdRequest = new TradeGetByIdRequest();
        tradeGetByIdRequest.setTid(orderId);
        TradeGetByIdResponse context = tradeQueryProvider.getById(tradeGetByIdRequest).getContext();
//        TradeVO tradeVO = tradeResponse.getContext().getTradeVO();
        if (CollectionUtils.isEmpty(context.getTradeVO().getTradeVOList())){
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }

        //判断订单中是否包含虚拟商品和电子卡券，防止用户刷商品
        if (flag){
            List<TradeItemVO> tradeItemVOList = context.getTradeVO().getGifts().stream().filter(tradeItemVO ->
                    tradeItemVO.getGoodsType().equals(GoodsType.VIRTUAL_GOODS) || tradeItemVO.getGoodsType()
                            .equals(GoodsType.VIRTUAL_COUPON)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(tradeItemVOList)){
                throw new SbcRuntimeException("K-050319");
            }
        }
        return context;
    }


    public abstract BaseResponse interceptorErpDeliverStatus(ReturnOrderVO returnOrderVO, Boolean flag);
}
