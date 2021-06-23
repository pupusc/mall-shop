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
public class AllRoleMenuInfoListResponse implements Serializable {
    private static final long serialVersionUID = 4734014779315554505L;
    /**
     * 菜单列表
     */
    @ApiModelProperty(value = "菜单列表")
    private List<MenuInfoVO> menuInfoVOList = new ArrayList<>();
}
