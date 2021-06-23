package com.wanmi.sbc.account.api.request.finance.record;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>获取单条结算单信息request</p>
 * Created by daiyitian on 2018-10-13-下午6:29.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettlementGetByIdRequest extends AccountBaseRequest{
    private static final long serialVersionUID = -3013009710823545987L;

    /**
     * 结算单编号
     */
    @ApiModelProperty(value = "结算单编号")
    @NotNull
    private Long settleId;

}
