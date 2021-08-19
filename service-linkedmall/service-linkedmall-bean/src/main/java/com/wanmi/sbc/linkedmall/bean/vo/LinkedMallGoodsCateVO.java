package com.wanmi.sbc.linkedmall.bean.vo;

import com.wanmi.sbc.common.enums.ThirdPlatformType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class LinkedMallGoodsCateVO {
    /**
     * linkedmall分类id
     */
    @ApiModelProperty("linkedmall分类id")
    private Long cateId;
    /**
     * linkedmall分类名称
     */
    @ApiModelProperty("linkedmall分类名称")
    private String cateName;
    /**
     * 分类的父级id（一级分类的父级id为0）
     */
    @ApiModelProperty("分类的父级id（一级分类的父级id为0）")
    private Long cateParentId;
    /**
     * 当前分类的路径
     */
    @ApiModelProperty("当前分类的路径")
    private String catePath;
    /**
     * 当前分类的层级
     */
    @ApiModelProperty("当前分类的层级")
    private Integer cateGrade;

    @ApiModelProperty("来源，0代表linkedmall")
    private ThirdPlatformType thirdPlatformType;
}
