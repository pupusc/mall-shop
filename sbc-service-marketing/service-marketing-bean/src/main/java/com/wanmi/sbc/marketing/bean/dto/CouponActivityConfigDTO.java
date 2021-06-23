package com.wanmi.sbc.marketing.bean.dto;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Enumerated;
import java.io.Serializable;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-23
 */
@ApiModel
@Data
public class CouponActivityConfigDTO implements Serializable {

    private static final long serialVersionUID = -748730939348500318L;

    /**
     * 优惠券活动配置表id
     */
    @ApiModelProperty(value = "优惠券活动配置id")
    private String activityConfigId;

    /**
     * 活动id
     */
    @ApiModelProperty(value = "优惠券活动id")
    private String activityId;

    /**
     * 优惠券id
     */
    @ApiModelProperty(value = "优惠券id")
    private String couponId;


    /**
     * 优惠券总张数
     */
    @ApiModelProperty(value = "优惠券总张数")
    private Long totalCount;

    /**
     * 是否有剩余, 1 有，0 没有
     */
    @ApiModelProperty(value = "是否有剩余")
    @Enumerated
    private DefaultFlag hasLeft;
}
