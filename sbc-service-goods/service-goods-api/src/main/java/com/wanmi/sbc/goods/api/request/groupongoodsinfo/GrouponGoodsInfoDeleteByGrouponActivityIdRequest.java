package com.wanmi.sbc.goods.api.request.groupongoodsinfo;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

/**
 * 根据拼团活动ID删除拼团活动商品-request对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("根据拼团活动ID删除拼团活动商品-request对象")
public class GrouponGoodsInfoDeleteByGrouponActivityIdRequest extends BaseRequest {

    private static final long serialVersionUID = 4449911275417136247L;

    /**
     * 拼团活动ID
     */
    @ApiModelProperty(value = "拼团活动ID")
    @NotBlank
    private String grouponActivityId;

}
