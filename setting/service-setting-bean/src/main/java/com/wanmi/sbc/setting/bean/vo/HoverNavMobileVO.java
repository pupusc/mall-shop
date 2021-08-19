package com.wanmi.sbc.setting.bean.vo;

import com.wanmi.sbc.setting.bean.enums.UsePageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>移动端悬浮导航栏VO</p>
 *
 * @author dyt
 * @date 2020-04-29 14:28:21
 */
@ApiModel
@Data
public class HoverNavMobileVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    private Long storeId;

    /**
     * 应用页面
     */
    @ApiModelProperty(value = "应用页面")
    private List<UsePageType> usePages;

    /**
     * 导航项
     */
    @ApiModelProperty(value = "导航项")
    private List<HoverNavMobileItemVO> navItems;
}