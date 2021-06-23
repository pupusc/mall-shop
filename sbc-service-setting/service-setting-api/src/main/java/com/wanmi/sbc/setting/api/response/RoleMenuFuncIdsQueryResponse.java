package com.wanmi.sbc.setting.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色-菜单功能返回类
 * Author: bail
 * Time: 2017/12/29
 */
@ApiModel
@Data
public class RoleMenuFuncIdsQueryResponse implements Serializable {

    private static final long serialVersionUID = 3204421572562940531L;
    /**
     * 拥有的菜单标识List
     */
    @ApiModelProperty(value = "拥有的菜单标识List")
    private List<String> menuIdList = new ArrayList<>();

    /**
     * 拥有的功能标识List
     */
    @ApiModelProperty(value = "拥有的功能标识List")
    private List<String> functionIdList = new ArrayList<>();

}
