package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.setting.bean.vo.RoleInfoAndMenuInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleInfoAndMenuInfoListResponse implements Serializable {
    private static final long serialVersionUID = -1871706324098744741L;
    /**
     * 一级菜单列表
     */
    @ApiModelProperty(value = "一级菜单列表")
    private List<RoleInfoAndMenuInfoVO> roleInfoAndMenuInfoVOList;
}
