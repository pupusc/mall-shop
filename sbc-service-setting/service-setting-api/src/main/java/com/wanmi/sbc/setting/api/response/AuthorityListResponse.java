package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.setting.bean.vo.AuthorityVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApiModel
@Data
public class AuthorityListResponse implements Serializable {
    private static final long serialVersionUID = -7983517333282189159L;
    /**
     * 权限列表
     */
    @ApiModelProperty(value = "权限列表")
    private List<AuthorityVO> authorityVOList = new ArrayList<>();
}
