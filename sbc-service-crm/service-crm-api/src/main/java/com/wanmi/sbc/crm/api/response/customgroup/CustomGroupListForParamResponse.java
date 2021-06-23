package com.wanmi.sbc.crm.api.response.customgroup;

import com.wanmi.sbc.crm.bean.vo.CustomGroupVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomGroupListForParamResponse {

    /**
     * 系统人群列表查询结果
     */
    @ApiModelProperty(value = "系统人群列表查询结果")
    private List<CustomGroupVo> customGroupVos;
}
