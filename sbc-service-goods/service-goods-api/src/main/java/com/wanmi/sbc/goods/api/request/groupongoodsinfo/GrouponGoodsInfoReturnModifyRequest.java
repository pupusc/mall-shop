package com.wanmi.sbc.goods.api.request.groupongoodsinfo;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>拼团商品成团后的退单信息修改参数bean</p>
 * Created by of628-wenzhi on 2019-05-27-20:06.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class GrouponGoodsInfoReturnModifyRequest extends BaseRequest {
    private static final long serialVersionUID = 7654030112319268784L;

    /**
     * 活动id
     */
    @NotNull
    @ApiModelProperty
    private String grouponActivityId;

    /**
     * skuid
     */
    @NotNull
    @ApiModelProperty
    private String goodsInfoId;

    /**
     * 成团后的退单数，增量数
     */
    @NotNull
    @Min(0)
    private Integer num;

    /**
     * 成团后的退单金额，增量金额
     */
    @NotNull
    @Min(0)
    private BigDecimal amount;
}
