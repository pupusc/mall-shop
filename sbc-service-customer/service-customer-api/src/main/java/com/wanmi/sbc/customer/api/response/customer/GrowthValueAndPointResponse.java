package com.wanmi.sbc.customer.api.response.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>完善信息页面初始化所需要的数据</p>
 *
 * @author zhangwenchang
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrowthValueAndPointResponse implements Serializable {
    private static final long serialVersionUID = 2377965385048125989L;

    /**
     * 完善信息可获得的积分值
     */
    @ApiModelProperty(value = "完善信息可获得的积分值")
    private Long point;

    /**
     * 完善信息获取积分配置是否开启
     */
    @ApiModelProperty(value = "完善信息获取积分配置是否开启")
    private Boolean pointFlag;

    /**
     * 完善信息获取成长值配置是否开启
     */
    @ApiModelProperty(value = "完善信息获取成长值配置是否开启")
    private Boolean growthFlag;

    /**
     * 完善信息可获得的成长值
     */
    @ApiModelProperty(value = "完善信息可获得的成长值")
    private Long growthValue;
}
