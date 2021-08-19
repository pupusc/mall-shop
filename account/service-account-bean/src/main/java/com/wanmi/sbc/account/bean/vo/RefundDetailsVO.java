package com.wanmi.sbc.account.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * <p>退款明细查询返回参数结构</p>
 * Created by of628-wenzhi on 2017-12-08-下午5:14.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class RefundDetailsVO extends AccountDetailsVO {

    private static final long serialVersionUID = -3707616313043861357L;

    /**
     * 退单号
     */
    @ApiModelProperty(value = "退单号")
    private String returnOrderCode;
}
