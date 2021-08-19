package com.wanmi.sbc.setting.api.response.popupadministration;


import com.wanmi.sbc.setting.bean.vo.PopupAdministrationVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>弹窗管理结果</p>
 * @author weiwenhao
 * @date 2020-04-22
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageManagementResponse implements Serializable {


    private static final long serialVersionUID = 1389064714952102675L;
    /**
     * 应用页名称
     */
    @ApiModelProperty(value = "应用页名称")
    private String pageManagementName;

    /**
     * 弹窗管理分页列表
     */
    @ApiModelProperty(value = "弹窗管理分页列表")
    private List<PopupAdministrationVO> popupAdministrationVOS;


}
