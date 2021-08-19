package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.setting.bean.vo.MenuInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@ApiModel
@Data
public class MenuAndAuthorityListResponse implements Serializable {
    private static final long serialVersionUID = -7156798580354275884L;
    /**
     * 系统所有的菜单,功能,权限
     */
    @ApiModelProperty(value = "系统所有的菜单,功能,权限")
    private List<MenuInfoVO> menuInfoVOList = new ArrayList<>();
}
