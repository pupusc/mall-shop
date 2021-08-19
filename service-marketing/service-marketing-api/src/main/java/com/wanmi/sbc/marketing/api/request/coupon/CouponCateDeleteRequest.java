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

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: songhanlin
 * @Date: Created In 2:24 PM 2018/9/13
 * @Description: 优惠券分类删除Request
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponCateDeleteRequest implements Serializable {

    private static final long serialVersionUID = 8629276777296197771L;

    /**
     * 优惠券分类Id
     */
    @ApiModelProperty(value = "优惠券分类Id")
    private String couponCateId;

    /**
     * 删除人
     */
    @ApiModelProperty(value = "删除人")
    private String delPerson;

    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime delTime;

}
