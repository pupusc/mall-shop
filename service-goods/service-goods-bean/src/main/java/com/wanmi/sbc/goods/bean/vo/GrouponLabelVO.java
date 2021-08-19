package com.wanmi.sbc.goods.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 营销标签
 * Created by dyt on 2018/2/28.
 */
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GrouponLabelVO implements Serializable {

    private static final long serialVersionUID = -3098691550938179678L;

    /**
     * 营销编号
     */
    @ApiModelProperty(value = "营销编号")
    private String grouponActivityId;


    /**
     * 促销描述
     */
    @ApiModelProperty(value = "促销描述")
    private String marketingDesc;

}
