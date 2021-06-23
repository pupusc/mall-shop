package com.wanmi.sbc.marketing.api.request.pointscoupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>单个删除积分兑换券表请求参数</p>
 *
 * @author yang
 * @date 2019-06-11 10:07:09
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsCouponDelByIdRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 积分兑换券id
     */
    @ApiModelProperty(value = "积分兑换券id")
    @NotNull
    private Long pointsCouponId;

    /**
     * 操作人id
     */
    @ApiModelProperty(value = "操作人id")
    private String operatorId;
}