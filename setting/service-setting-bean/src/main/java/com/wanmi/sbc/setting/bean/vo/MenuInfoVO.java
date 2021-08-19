package com.wanmi.sbc.setting.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 菜单权限返回类
 * Author: bail
 * Time: 2017/12/28.16:34
 */
@ApiModel
@Data
public class MenuInfoVO implements Serializable {
    private static final long serialVersionUID = -286469873060052827L;
    /**
     * 节点标识
     */
    @ApiModelProperty(value = "节点标识")
    private String id;

    /**
     * 父节点标识
     */
    @ApiModelProperty(value = "父节点标识")
    private String pid;

    /**
     * 菜单/权限id
     */
    @ApiModelProperty(value = "菜单/权限id")
    private String realId;

    /**
     * 显示名
     */
    @ApiModelProperty(value = "显示名")
    private String title;

    /**
     * 层级(权限默认层级999)
     */
    @ApiModelProperty(value = "层级-权限默认层级999")
    private Integer grade;

    /**
     * 菜单图标
     */
    @ApiModelProperty(value = "权限默认层级999")
    private String icon;

    /**
     * 权限名
     */
    @ApiModelProperty(value = "权限名")
    private String authNm;

    /**
     * 权限url路径
     */
    @ApiModelProperty(value = "权限url路径")
    private String url;

    /**
     * url请求类型
     */
    @ApiModelProperty(value = "url请求类型")
    private String reqType;

    /**
     * 权限备注
     */
    @ApiModelProperty(value = "权限备注")
    private String authRemark;

    /**
     * 是否为菜单路径
     */
    @ApiModelProperty(value = "是否为菜单路径")
    private Integer isMenuUrl;

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    private Integer sort;
}
