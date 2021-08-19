package com.wanmi.sbc.setting.bean.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>移动端悬浮导航栏实体类</p>
 *
 * @author dyt
 * @date 2020-04-29 14:28:21
 */
@Data
public class HoverNavMobileItemVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String imgSrc;

    /**
     * 导航名称
     */
    @ApiModelProperty(value = "导航名称")
    private String title;

    /**
     * 落地页的json字符串
     */
    @ApiModelProperty(value = "落地页的json字符串")
    private String linkInfoPage;
}