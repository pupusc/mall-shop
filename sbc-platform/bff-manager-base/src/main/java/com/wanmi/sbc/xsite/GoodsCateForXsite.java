package com.wanmi.sbc.xsite;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class GoodsCateForXsite {

    @ApiModelProperty(value = "分类编号")
    private String id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "父类编号")
    private String parentId;

    @ApiModelProperty(value = "分类层次")
    private Integer depth;

    @ApiModelProperty(value = "分类路径")
    private String path;

    @ApiModelProperty(value = "拼音")
    private String pinYin;

    @ApiModelProperty(value = "简拼")
    private String simplePinYin;

    @ApiModelProperty(value = "魔方建站参数")
    private String addOn;

    @ApiModelProperty(value = "魔方建站参数")
    private String catPriceRange;

    @ApiModelProperty(value = "魔方建站参数")
    private String chainMasterId;

    @ApiModelProperty(value = "魔方建站参数")
    private Integer corder;

    @ApiModelProperty(value = "魔方建站参数")
    private String description;

    @ApiModelProperty(value = "魔方建站参数")
    private String detailTemplate;

    @ApiModelProperty(value = "魔方建站参数")
    private Integer displaySite;

    @ApiModelProperty(value = "魔方建站参数")
    private String fromId;

    @ApiModelProperty(value = "魔方建站参数")
    private String fromSys;

    @ApiModelProperty(value = "魔方建站参数")
    private String img;

    @ApiModelProperty(value = "魔方建站参数")
    private Integer isLeaf;

    @ApiModelProperty(value = "魔方建站参数")
    private Integer itemCount;

    @ApiModelProperty(value = "魔方建站参数")
    private Integer leaf;

    @ApiModelProperty(value = "魔方建站参数")
    private Integer marketableCount;

    @ApiModelProperty(value = "魔方建站参数")
    private String optCode;

    @ApiModelProperty(value = "魔方建站参数")
    private String optTime;

    @ApiModelProperty(value = "魔方建站参数")
    private Integer priceSearchFlag;

    @ApiModelProperty(value = "魔方建站参数")
    private Integer state;

    @ApiModelProperty(value = "魔方建站参数")
    private Integer typeId;

    @ApiModelProperty(value = "魔方建站参数")
    private String typeName;

    @ApiModelProperty(value = "魔方建站参数")
    private String typeTemplate;

}
