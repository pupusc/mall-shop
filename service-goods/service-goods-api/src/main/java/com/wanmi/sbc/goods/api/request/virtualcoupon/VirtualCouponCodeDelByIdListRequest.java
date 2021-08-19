package com.wanmi.sbc.goods.api.request.virtualcoupon;

import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import lombok.*;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <p>批量删除券码请求参数</p>
 *
 * @author 梁善
 * @date 2021-01-25 16:14:42
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualCouponCodeDelByIdListRequest extends GoodsBaseRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 批量删除-券码IDList
     */
    @ApiModelProperty(value = "批量删除-券码IDList")
    @NotEmpty
    private List<Long> idList;

    @ApiModelProperty(value = "卡券ID")
    @NotNull
    private Long couponId;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "修改人")
    private String updatePerson;

}
