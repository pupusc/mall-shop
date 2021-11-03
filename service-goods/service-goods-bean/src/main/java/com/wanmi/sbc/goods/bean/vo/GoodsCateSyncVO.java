package com.wanmi.sbc.goods.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;

@Data
public class GoodsCateSyncVO {
    @ApiModelProperty("ID")
    private Integer id;
    @ApiModelProperty("父类ID")
    private Integer parnetId;
    @ApiModelProperty("类目名称")
    private String name;
    @ApiModelProperty(value = "类目对应标签",hidden = true)
    private String labelIds;
}
