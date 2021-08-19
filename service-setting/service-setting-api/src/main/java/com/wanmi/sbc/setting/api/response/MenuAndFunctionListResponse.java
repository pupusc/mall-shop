package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.setting.bean.vo.MenuInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单权限返回类
 * Author: bail
 * Time: 2017/12/28.16:34
 */
@ApiModel
@Data
public class MenuAndFunctionListResponse implements Serializable {
    private static final long serialVersionUID = -3364468641156806354L;
    /**
     * 菜单及功能列表
     */
    @ApiModelProperty(value = "菜单及功能列表")
    private List<MenuInfoVO> menuInfoVOList = new ArrayList<>();
}
