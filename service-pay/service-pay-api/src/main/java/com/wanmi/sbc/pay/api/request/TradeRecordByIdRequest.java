package com.wanmi.sbc.pay.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.validation.constraints.NotBlank;

/**
 * <p>根据id查询单笔交易记录request</p>
 * Created by of628-wenzhi on 2018-08-13-下午3:47.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeRecordByIdRequest extends PayBaseRequest{
    private static final long serialVersionUID = -7335560540724041155L;

    /**
     * 交易记录id
     */
    @ApiModelProperty(value = "交易记录id")
    @NotBlank
    private String recodId;
}
