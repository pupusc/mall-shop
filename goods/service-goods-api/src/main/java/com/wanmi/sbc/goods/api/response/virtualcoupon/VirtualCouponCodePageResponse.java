package com.wanmi.sbc.goods.api.response.virtualcoupon;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.VirtualCouponCodeVO;
import com.wanmi.sbc.goods.bean.vo.VirtualCouponVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>券码分页结果</p>
 * @author 梁善
 * @date 2021-01-25 16:14:42
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualCouponCodePageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 券码分页结果
     */
    @ApiModelProperty(value = "券码分页结果")
    private MicroServicePage<VirtualCouponCodeVO> virtualCouponCodeVOPage;

    /**
     * 卡券信息
     */
    @ApiModelProperty("卡券信息")
    private VirtualCouponVO virtualCoupon;
}
