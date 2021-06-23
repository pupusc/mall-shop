package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.goods.bean.enums.DeliveryCycle;
import com.wanmi.sbc.goods.bean.enums.DeliveryPlan;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class VerifyCycleBuyRequest implements Serializable {

    /**
     *商品spuId
     */
    @ApiModelProperty(value = "商品spuId")
    @NotBlank
    private String goodsId;

    /**
     * 赠品列表
     */
    @ApiModelProperty(value = "赠品列表")
    private List<String> gifts = new ArrayList<>();

    /**
     *  配送方案 0:商家主导配送 1:客户主导配送
     */
    @ApiModelProperty(value = "配送方案")
    private DeliveryPlan deliveryPlan;

    /**
     * 配送周期 0:每日一期 1:每周一期 2:每月一期
     */
    @ApiModelProperty(value = "配送周期 0:每日一期 1:每周一期 2:每月一期")
    private DeliveryCycle deliveryCycle;

    /**
     * 用户选择的发货日期
     */
    @ApiModelProperty(value = "用户选择的发货日期")
    private String sendDateRule;
}
