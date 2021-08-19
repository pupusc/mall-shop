package com.wanmi.sbc.setting.api.request.popupadministration;


import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>弹窗详情查询</p>
 * @author weiwenhao
 * @date 2020-04-23
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageManagementGetIdRequest extends SettingBaseRequest {

    private static final long serialVersionUID = -516508803082218913L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @NotNull
    private Long popupId;

}
