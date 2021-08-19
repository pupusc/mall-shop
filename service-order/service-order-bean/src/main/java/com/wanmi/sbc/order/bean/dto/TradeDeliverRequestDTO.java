package com.wanmi.sbc.order.bean.dto;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.bean.vo.LogisticsVO;
import com.wanmi.sbc.order.bean.vo.ShippingItemVO;
import com.wanmi.sbc.order.bean.vo.TradeDeliverVO;
import com.wanmi.sbc.setting.bean.vo.ExpressCompanyVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */
@Data
@ApiModel
public class TradeDeliverRequestDTO extends BaseQueryRequest {

    /**
     * 物流单号
     */
    @ApiModelProperty(value = "物流单号")
    private String deliverNo;

    /**
     * 物流ID
     */
    @ApiModelProperty(value = "物流ID")
    private String deliverId;

    /**
     * 发货信息
     */
    @ApiModelProperty(value = "发货信息")
    @Valid
    private List<ShippingItemDTO> shippingItemList = new ArrayList<>();

    /**
     * 赠品信息
     */
    @ApiModelProperty(value = "赠品信息")
    @Valid
    private List<ShippingItemDTO> giftItemList = new ArrayList<>();

    /**
     * 发货时间
     */
    @ApiModelProperty(value = "发货时间")
    private String deliverTime;

    /**
     * @return
     */
    public TradeDeliverVO toTradeDevlier(ExpressCompanyVO expressCompany) {
        LogisticsVO logistics = null;
        if (expressCompany != null) {
            logistics = LogisticsVO.builder()
                    .logisticCompanyId(expressCompany.getExpressCompanyId().toString())
                    .logisticCompanyName(expressCompany.getExpressName())
                    .logisticNo(deliverNo)
                    .logisticStandardCode(expressCompany.getExpressCode())
                    .build();

            logistics.setLogisticNo(deliverNo);
        }
        TradeDeliverVO tradeDeliver = new TradeDeliverVO();
        tradeDeliver.setLogistics(logistics);
        tradeDeliver.setShippingItems(KsBeanUtil.convertList(shippingItemList, ShippingItemVO.class));
        tradeDeliver.setGiftItemList(KsBeanUtil.convertList(giftItemList, ShippingItemVO.class));
        tradeDeliver.setDeliverTime(DateUtil.parseDay(deliverTime));
        return tradeDeliver;
    }

}
