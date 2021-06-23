package com.wanmi.sbc.account.api.request.finance.record;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * <p>删除结算和结算明细request</p>
 * Created by daiyitian on 2018-10-13-下午6:29.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettlementDeleteRequest extends AccountBaseRequest{
    private static final long serialVersionUID = -3013009710823545987L;

    /**
     * 店铺编号
     */
    @ApiModelProperty(value = "店铺编号")
    @NotNull
    private Long storeId;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @NotBlank
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @NotBlank
    private String endTime;
}
