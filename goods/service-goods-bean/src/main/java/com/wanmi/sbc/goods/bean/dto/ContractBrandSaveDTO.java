package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 签约品牌更新请求
 * Created by sunkun on 2017/10/31.
 */
@ApiModel
@Data
public class ContractBrandSaveDTO implements Serializable {

    private static final long serialVersionUID = -8756169481298950247L;

    /**
     * 签约品牌分类
     */
    @ApiModelProperty(value = "签约品牌分类")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5_a-zA-Z0-9_]{1,30}$")
    private Long contractBrandId;


    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    private String name;

    /**
     * 品牌别名
     */
    @ApiModelProperty(value = "品牌别名")
    private String nickName;

    /**
     * 品牌logo
     */
    @ApiModelProperty(value = "品牌logo")
    private String logo;

    /**
     * 店铺主键
     */
    @ApiModelProperty(value = "店铺主键")
    private Long storeId;


    /**
     * 品牌主键
     */
    @ApiModelProperty(value = "品牌主键")
    private Long brandId;

    /**
     * 待审核品牌主键
     */
    @ApiModelProperty(value = "待审核品牌主键")
    private Long checkBrandId;


    /**
     * 授权图片路径
     */
    @ApiModelProperty(value = "授权图片路径")
    private String authorizePic;
}
