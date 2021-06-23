package com.wanmi.sbc.elastic.bean.vo.goods;

import com.wanmi.sbc.common.enums.DeleteFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class GoodsLabelNestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签id
     */
    @ApiModelProperty(value = "标签id")
    private Long goodsLabelId;

    /**
     * 标签名称
     */
    @ApiModelProperty(value = "标签名称")
    private String labelName;

    /**
     * 排序规则
     */
    @ApiModelProperty(value = "排序规则")
    private Integer labelSort;

    /**
     * 前端是否展示 0: 关闭 1:开启
     */
    @ApiModelProperty(value = "前端是否展示 0: 关闭 1:开启")
    private Boolean labelVisible;

    /**
     * 删除标识 0:未删除1:已删除
     */
    @ApiModelProperty(value = "删除标识 0:未删除1:已删除")
    private DeleteFlag delFlag;
}
