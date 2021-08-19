package com.wanmi.sbc.customer.api.request.fandeng;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: sbc-background
 * @description: 樊登积分锁定入参
 * @author: Mr.Tian
 * @create: 2021-01-28 13:52
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FanDengKnowledgeLockRequest implements Serializable {

    private static final long serialVersionUID = -4774626069854992691L;

    /**
     * 会员编号
     */
    @ApiModelProperty(value = "会员编号")
    @NotBlank
    private String userNo;

    /**
     * 知豆
     */
    @ApiModelProperty(value = "知豆")
    @NotNull
    private long beans;


    /**
     * 数据来源类型 1:商城订单
     */
    @ApiModelProperty(value = "数据来源id")
    private String sourceId;
    /**
     * 变更描述
     */
    @ApiModelProperty(value = "变更描述")
    @NotBlank
    private String desc;


}
