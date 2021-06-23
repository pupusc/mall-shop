package com.wanmi.sbc.marketing.api.response.pointscoupon;

import com.wanmi.sbc.marketing.bean.vo.PointsCouponVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>积分兑换券表修改结果</p>
 * @author yang
 * @date 2019-06-11 10:07:09
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsCouponModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的积分兑换券表信息
     */
    @ApiModelProperty(value = "已修改的积分兑换券表信息")
    private PointsCouponVO pointsCouponVO;
}
