package com.wanmi.sbc.setting.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午10:28 2018/11/19
 * @Description:
 */
@ApiModel
@Data
public class AppShareSettingGetResponse implements Serializable {

    private static final long serialVersionUID = -3493848746293180135L;

    /**
     * app分享开关
     */
    @ApiModelProperty(value = "app分享开关")
    private boolean enabled;

    /**
     * app分享标题
     */
    @ApiModelProperty(value = "app分享标题")
    private String title;

    /**
     * app分享描述
     */
    @ApiModelProperty(value = "app分享描述")
    private String desc;

    /**
     * app图标
     */
    @ApiModelProperty(value = "app图标")
    private String icon;

    /**
     * ios下载包地址
     */
    @ApiModelProperty(value = "ios下载包地址")
    private String iosUrl;

    /**
     * android下载包地址
     */
    @ApiModelProperty(value = "android下载包地址")
    private String androidUrl;

    /**
     * app下载页面链接
     */
    @ApiModelProperty(value = "app下载页面链接")
    private String downloadUrl;

    /**
     * app下载二维码
     */
    @ApiModelProperty(value = "app下载二维码")
    private String downloadImg;

    /**
     * app分享页背景图
     */
    @ApiModelProperty(value = "app分享页背景图")
    private String shareImg;

}
