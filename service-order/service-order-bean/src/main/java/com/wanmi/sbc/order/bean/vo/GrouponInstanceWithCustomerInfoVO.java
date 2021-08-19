package com.wanmi.sbc.order.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.enums.GrouponOrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单状态
 * Created by jinwei on 14/3/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class GrouponInstanceWithCustomerInfoVO implements Serializable {


    private static final long serialVersionUID = 8297010266465448728L;
    /**
     * 团号
     */
    @ApiModelProperty(value = "团号")
    private String grouponNo;


    /**
     *团截止时间
     */
    @ApiModelProperty(value = "团截止时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;


    /**
     * 拼团人数
     */
    @ApiModelProperty(value = "拼团人数")
    private Integer grouponNum;

    /**
     * 参团人数
     */
    @ApiModelProperty(value = "参团人数参团人数")
    private Integer joinNum;

    /**
     * 团长用户id
     */
    @ApiModelProperty(value = "")
    private String customerId;

    /**
     * 拼团状态
     */
    @ApiModelProperty(value = "")
    private GrouponOrderStatus grouponStatus;


    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String customerName;


    @ApiModelProperty(value = "头像路径")
    private String headimgurl;

}
