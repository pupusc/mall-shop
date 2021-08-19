package com.wanmi.sbc.marketing.bean.vo;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 优惠券活动关联配置表
 */
@Data
public class CouponCateRelaVO implements Serializable {

    /**
     *  优惠券id
     */
    private String couponId ;

    /**
     *  优惠券分类名称
     */
    private List<String> couponCateName;

    /**
     * 是否已经绑定营销活动 0否 1是
     */
    @ApiModelProperty(value = "是否已经绑定营销活动")
    private DefaultFlag isFree;

}
