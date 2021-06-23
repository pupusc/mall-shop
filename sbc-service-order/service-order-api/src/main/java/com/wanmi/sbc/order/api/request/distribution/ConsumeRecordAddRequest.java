package com.wanmi.sbc.order.api.request.distribution;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Description: 消费记录新增请求
 * @Autho qiaokang
 * @Date：2019-03-05 18:44:58
 */
@Data
@ApiModel
public class ConsumeRecordAddRequest implements Serializable {

    private static final long serialVersionUID = 3623383356597307654L;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    @NotNull
    private String orderId;

    /**
     * 消费额
     */
    @ApiModelProperty(value = "消费额")
    @NotNull
    private BigDecimal consumeSum;

    @ApiModelProperty(value = "订单状态")
    private String flowState;

    /**
     * 有效消费额
     */
    @ApiModelProperty(value = "有效消费额")
    private BigDecimal validConsumeSum;

    /**
     * 店铺Id
     */
    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

    /**
     * 分销员id
     */
    @ApiModelProperty(value = "分销员id")
    private String distributionCustomerId;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @NotNull
    private LocalDateTime orderCreateTime;

    /**
     * 购买人的客户id
     */
    @ApiModelProperty(value = "购买人的客户id")
    private String customerId;

    /**
     * 客户姓名
     */
    @ApiModelProperty(value = "客户姓名")
    private String customerName;

    /**
     * 客户头像
     */
    @ApiModelProperty(value = "客户头像")
    private String headImg;

}
