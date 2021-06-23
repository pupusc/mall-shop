package com.wanmi.sbc.setting.api.request.popupadministration;


import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>弹窗管理新增参数</p>
 * @author weiwenhao
 * @date 2020-04-22
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopupAdministrationSortRequest extends SettingBaseRequest {

    private static final long serialVersionUID = 368866220813292663L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @NotNull
    private Long popupId;

    /**
     * 应用页面
     */
    @ApiModelProperty(value = "应用页面")
    @NotNull
    private String applicationPageName;

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    @NotNull
    private Long sortNumber;

}
