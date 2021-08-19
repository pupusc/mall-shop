package com.wanmi.sbc.setting.api.request.popupadministration;


import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.setting.bean.enums.PopupStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>弹窗管理新增参数</p>
 * @author weiwenhao
 * @date 2020-04-21
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopupAdministrationPageRequest  extends BaseQueryRequest{


    /**
     * 查询类型，0：全部，1：进行中，2：暂停中，3：未开始，4：已结束
     */
    @ApiModelProperty(value = "查询类型")
    @NotNull
    private PopupStatus queryTab;

    @ApiModelProperty(value = "弹窗名称")
    private String popupName;

}
