package com.wanmi.sbc.order.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import com.wanmi.sbc.goods.bean.enums.DeliveryCycle;
import com.wanmi.sbc.goods.bean.enums.DeliveryPlan;
import com.wanmi.sbc.goods.bean.enums.GiftGiveMethod;
import com.wanmi.sbc.goods.bean.vo.CycleBuySendDateRuleVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 周期购信息
 */
@Data
@ApiModel
public class CycleBuyInfoVO {

    /**
     * 配送周期 0:每日一期 1:每周一期 2:每月一期
     */
    @ApiModelProperty(value = "配送周期 0:每日一期 1:每周一期 2:每月一期")
    private DeliveryCycle deliveryCycle;

    /**
     * 用户选择的发货日期
     */
    @ApiModelProperty(value = "用户选择的发货日期")
    private CycleBuySendDateRuleVO cycleBuySendDateRule;

    /**
     * 总期数
     */
    @ApiModelProperty(value = "总期数")
    private Integer cycleNum;

    /**
     * 下次发货时间
     */
    @ApiModelProperty(value = "下次发货时间")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate nextDeliverTime;

    /**
     * 周期购赠品
     */
    @ApiModelProperty(value = "周期购赠品")
    private List<String> cycleBuyGifts;

    /**
     *  配送方案 0:商家主导配送 1:客户主导配送
     */
    @ApiModelProperty(value = "配送方案")
    private DeliveryPlan deliveryPlan;

    /**
     * 赠送方式：0：默认全送 1:可选一种
     */
    @ApiModelProperty(value = "赠送方式：0：默认全送 1:可选一种")
    private GiftGiveMethod giftGiveMethod;
}
