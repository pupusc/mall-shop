package com.wanmi.sbc.goods.api.response.virtualcoupon;

import com.wanmi.sbc.goods.bean.vo.VirtualCouponVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>卡券新增结果</p>
 * @author 梁善
 * @date 2021-01-25 10:19:19
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualCouponAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的卡券信息
     */
    @ApiModelProperty(value = "已新增的卡券信息")
    private VirtualCouponVO virtualCouponVO;
}
