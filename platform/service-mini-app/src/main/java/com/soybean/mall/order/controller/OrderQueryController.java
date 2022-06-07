package com.soybean.mall.order.controller;

import com.soybean.mall.order.common.CommonPackageModel;
import com.soybean.mall.order.request.OrderQueryReq;
import com.soybean.mall.order.response.order.WxOrderResp;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.constant.GoodsErrorCode;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.response.trade.TradeGetByIdResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/7 4:55 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RequestMapping("/wx/order/query")
@RestController
public class OrderQueryController {

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    /**
     * app - 获取订单信息
     * @menu 小程序订阅
     * @return
     */
    @PostMapping("/getWxOrder")
    public BaseResponse<WxOrderResp> getWxOrder(@RequestBody OrderQueryReq orderQueryReq) {
        if (StringUtils.isEmpty(orderQueryReq.getTid())) {
            throw new SbcRuntimeException("K-0000001", "参数误");
        }
        TradeGetByIdRequest tradeGetByIdRequest = new TradeGetByIdRequest();
        tradeGetByIdRequest.setTid(orderQueryReq.getTid());
        TradeGetByIdResponse context = tradeQueryProvider.getOrderById(tradeGetByIdRequest).getContext();
        if (context.getTradeVO() == null) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST, "订单不存在");
        }

        return BaseResponse.success(CommonPackageModel.packTradeVo2WxOrderResp(context.getTradeVO()));
    }
}
