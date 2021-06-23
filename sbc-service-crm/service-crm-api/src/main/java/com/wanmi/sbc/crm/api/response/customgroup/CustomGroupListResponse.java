package com.wanmi.sbc.crm.api.response.customgroup;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.crm.bean.vo.CustomGroupVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-13
 * \* Time: 16:49
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomGroupListResponse {
    /**
     * 系统人群分页查询结果
     */
    @ApiModelProperty(value = "自定义人群分页查询结果")
    private MicroServicePage<CustomGroupVo> customGroupPageResponse;
}
