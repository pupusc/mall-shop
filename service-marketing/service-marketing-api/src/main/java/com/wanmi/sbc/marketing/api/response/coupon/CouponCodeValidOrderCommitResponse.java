package com.wanmi.sbc.marketing.api.response.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 1:23 PM 2018/9/28
 * @Description: 查询优惠券码列表请求
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponCodeValidOrderCommitResponse implements Serializable {

    private static final long serialVersionUID = -1924077031934211666L;

    /**
     * 验证信息
     */
    @ApiModelProperty(value = "验证信息")
    private String validInfo;

    /**
     * 过期的优惠券
     */
    private  List<String> invalidCodeIds;
}
