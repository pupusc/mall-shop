package com.wanmi.sbc.account.api.response.company;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 商家收款账户统计entity
 * Created by daiyitian on 2017/11/30.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyAccountCountResponse implements Serializable {

    private static final long serialVersionUID = -2629974480473851524L;

    /**
     * 账户统计总数
     */
    @ApiModelProperty(value = "账户统计总数")
    private Integer count;
}
