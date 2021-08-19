package com.wanmi.sbc.customer.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午3:25 2019/6/13
 * @Description:
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributorDTO implements Serializable {

    private static final long serialVersionUID = -6178878064575518452L;

    /**
     * 会员ID
     */
    @ApiModelProperty("会员ID")
    private String customerId;

    /**
     * 提成
     */
    @ApiModelProperty("提成")
    @NotNull
    private BigDecimal commission;
}
