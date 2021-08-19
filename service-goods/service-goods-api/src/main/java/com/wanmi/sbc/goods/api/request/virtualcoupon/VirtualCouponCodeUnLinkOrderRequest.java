package com.wanmi.sbc.goods.api.request.virtualcoupon;

import com.wanmi.sbc.goods.bean.vo.VirtualCouponCodeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>券码取消关联订单参数</p>
 *
 * @author 梁善
 * @date 2021-01-25 16:14:42
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualCouponCodeUnLinkOrderRequest {

    /**
     * id,couponId, updatePerson 必填
     */
    @ApiModelProperty("券码ID列表")
    private List<VirtualCouponCodeVO> codeVOList;
}