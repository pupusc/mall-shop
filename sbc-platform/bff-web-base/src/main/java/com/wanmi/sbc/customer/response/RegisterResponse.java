package com.wanmi.sbc.customer.response;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.marketing.bean.enums.RegisterLimitType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 注册
 * Created by daiyitian on 15/11/28.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse implements Serializable{

    /**
     * 注册限制
     */
    @ApiModelProperty(value = "注册限制")
    private RegisterLimitType registerLimitType;

    /**
     * 是否开启社交分销
     */
    @ApiModelProperty(value = "是否开启社交分销")
    private DefaultFlag openFlag;
}
