package com.wanmi.sbc.marketing.bean.vo;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Enumerated;
import java.io.Serializable;

/**
 * <p>优惠券活动配置</p>
 * author: sunkun
 * Date: 2018-11-23
 */
@ApiModel
@Data
public class CouponActivityConfigVO implements Serializable {

    private static final long serialVersionUID = 4972632930845470518L;

    /**
     * 优惠券活动配置表id
     */
    @ApiModelProperty(value = "优惠券活动配置表id")
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
    @ApiModelProperty(value = "优惠券是否有剩余")
    @Enumerated
    private DefaultFlag hasLeft;
}
