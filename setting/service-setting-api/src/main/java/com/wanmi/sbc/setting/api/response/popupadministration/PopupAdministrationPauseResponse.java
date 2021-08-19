package com.wanmi.sbc.setting.api.response.popupadministration;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>弹窗管理结果</p>
 * @author weiwenhao
 * @date 2020-04-21
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopupAdministrationPauseResponse implements Serializable {

    private static final long serialVersionUID = 6722702012514946750L;

    /**
     * 热门搜索ID
     */
    @ApiModelProperty(value = "删除结果")
    private Integer resultNum;

}
