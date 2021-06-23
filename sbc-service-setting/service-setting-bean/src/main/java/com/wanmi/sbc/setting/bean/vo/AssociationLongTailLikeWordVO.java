package com.wanmi.sbc.setting.bean.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>联想词模糊实体类</p>
 * @author weiwenhao
 * @date 2020-04-16
 */
@Data
@ApiModel
public class AssociationLongTailLikeWordVO implements Serializable {

    /**
     * 主键id
     */
    @ApiModelProperty(value= "主键id")
    private Long associationLongTailWordId;

    /**
     * 联想词
     */
    @ApiModelProperty(value = "联想词")
    private String associationalWord;

    /**
     * 长尾词
     */
    @ApiModelProperty(value= "长尾词")
    private String[] longTailWord;

    /**
     * 关联搜索词id
     */
    @ApiModelProperty(value= "关联搜索词id")
    private Long searchAssociationalWordId;

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    private Long sortNumber;

}
