package com.wanmi.sbc.setting.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Author: bail
 * Time: 2017/11/13.10:22
 */
@ApiModel
@Data
public class RoleMenuAuthSaveRequest implements Serializable {

    private static final long serialVersionUID = 1811887574292086397L;
    /**
     * 角色标识
     */
    @ApiModelProperty(value = "角色标识")
    private Long roleInfoId;

    /**
     * 菜单标识List
     */
    @ApiModelProperty(value = "菜单标识List")
    private List<String> menuIdList;

    /**
     * 功能标识List
     */
    @ApiModelProperty(value = "功能标识List")
    private List<String> functionIdList;

}
