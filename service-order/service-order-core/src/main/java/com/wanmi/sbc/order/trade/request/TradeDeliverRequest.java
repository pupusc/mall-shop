package com.wanmi.sbc.order.trade.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.order.trade.model.entity.TradeDeliver;
import com.wanmi.sbc.order.trade.model.entity.value.Logistics;
import com.wanmi.sbc.order.trade.model.entity.value.ShippingItem;
import com.wanmi.sbc.setting.bean.vo.ExpressCompanyVO;
import lombok.Data;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */
@Data
public class TradeDeliverRequest extends BaseQueryRequest {

    /**
     * 物流单号
     */
    private String deliverNo;

    /**
     * 物流ID
     */
    private String deliverId;


    /**
     * 发货信息
     */
    @Valid
    private List<ShippingItem> shippingItemList = new ArrayList<>();


    /**
     * 赠品信息
     */
    @Valid
    private List<ShippingItem> giftItemList = new ArrayList<>();

    /**
     * 发货时间
     */
    private String deliverTime;

    /**
     *
     * @return
     */
    public TradeDeliver toTradeDevlier(ExpressCompanyVO expressCompany){
        Logistics logistics = null;
        if (expressCompany != null){
             logistics = Logistics.builder()
                    .logisticCompanyId(expressCompany.getExpressCompanyId().toString())
                    .logisticCompanyName(expressCompany.getExpressName())
                    .logisticNo(deliverNo)
                    .logisticStandardCode(expressCompany.getExpressCode())
                    .build();

            logistics.setLogisticNo(deliverNo);
        }
        TradeDeliver tradeDeliver = new TradeDeliver();
        tradeDeliver.setLogistics(logistics);
        tradeDeliver.setShippingItems(shippingItemList);
        tradeDeliver.setGiftItemList(giftItemList);
        tradeDeliver.setDeliverTime(DateUtil.parseDay( deliverTime));
        return tradeDeliver;
    }
}
