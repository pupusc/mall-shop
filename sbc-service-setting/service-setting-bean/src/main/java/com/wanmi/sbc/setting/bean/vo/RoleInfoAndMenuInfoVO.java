package com.wanmi.sbc.setting.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 菜单权限返回类
 * Author: bail
 * Time: 2017/12/28.16:34
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleInfoAndMenuInfoVO implements Serializable {
    private static final long serialVersionUID = -286469873060052827L;
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    private Long roleInfoId;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    /**
     * 一级菜单权限集合
     */
    @ApiModelProperty(value = "一级菜单权限集合")
    private String menuNames;
}
