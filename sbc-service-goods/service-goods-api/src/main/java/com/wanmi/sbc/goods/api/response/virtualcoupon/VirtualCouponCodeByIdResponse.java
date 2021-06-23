package com.wanmi.sbc.goods.api.response.virtualcoupon;

import com.wanmi.sbc.goods.bean.vo.VirtualCouponCodeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）券码信息response</p>
 * @author 梁善
 * @date 2021-01-25 16:14:42
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualCouponCodeByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 券码信息
     */
    @ApiModelProperty(value = "券码信息")
    private VirtualCouponCodeVO virtualCouponCodeVO;
}
