package com.wanmi.sbc.goods.api.response.virtualcoupon;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.VirtualCouponVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>卡券分页结果</p>
 * @author 梁善
 * @date 2021-01-25 10:19:19
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualCouponPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 卡券分页结果
     */
    @ApiModelProperty(value = "卡券分页结果")
    private MicroServicePage<VirtualCouponVO> virtualCouponVOPage;

}
