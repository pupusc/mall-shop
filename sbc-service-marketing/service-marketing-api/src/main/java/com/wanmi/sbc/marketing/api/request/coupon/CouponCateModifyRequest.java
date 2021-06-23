package com.wanmi.sbc.marketing.api.request.coupon;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: songhanlin
 * @Date: Created In 5:18 PM 2018/9/13
 * @Description: 优惠券分类修改Request
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponCateModifyRequest {

    /**
     * 优惠券分类Id
     */
    @ApiModelProperty(value = "优惠券分类Id")
    private String couponCateId;

    /**
     * 优惠券分类名称
     */
    @ApiModelProperty(value = "优惠券分类名称")
    private String couponCateName;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updatePerson;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;
}
