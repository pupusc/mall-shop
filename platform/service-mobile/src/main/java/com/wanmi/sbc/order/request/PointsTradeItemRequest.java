package com.wanmi.sbc.order.request;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

/**
 * <p>积分订单商品兑换请求结构</p>
 * Created by yinxianzhi on 2019-05-20-上午9:20.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class PointsTradeItemRequest extends BaseRequest {

    private static final long serialVersionUID = 3228778527828317959L;

    /**
     * 积分商品Id
     */
    @ApiModelProperty(value = "积分商品Id")
    @NotBlank
    private String pointsGoodsId;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    @Range(min = 1)
    private long num;

}
