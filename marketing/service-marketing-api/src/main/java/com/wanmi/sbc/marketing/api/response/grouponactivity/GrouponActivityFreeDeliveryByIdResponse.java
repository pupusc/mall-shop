package com.wanmi.sbc.marketing.api.response.grouponactivity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据id查询任意（包含已删除）拼团活动信息表信息response</p>
 *
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponActivityFreeDeliveryByIdResponse implements Serializable {

    private static final long serialVersionUID = -8510337128751546526L;

    /**
     * 是否包邮
     */
    @ApiModelProperty(value = "是否包邮")
    private boolean freeDelivery;
}
