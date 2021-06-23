package com.wanmi.sbc.setting.api.response.popupadministration;


import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.bean.vo.PopupAdministrationVO;
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
public class PopupAdministrationPageResponse implements Serializable {

    private static final long serialVersionUID = 6015241081175406030L;


    /**
     * 弹窗管理分页列表
     */
    @ApiModelProperty(value = "弹窗管理分页列表")
    private MicroServicePage<PopupAdministrationVO> popupAdministrationVOS;
}
