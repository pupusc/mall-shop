package com.wanmi.sbc.setting.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 物流记录
 * Created by dyt on 2020/4/17.
 */
@Data
public class PageInfoExtendVO implements Serializable {

    private static final long serialVersionUID = -6414564526969459575L;

    /**
     * pageId
     */
    @ApiModelProperty(value = "页面id")
    private String pageId;

    /**
     * 背景图
     */
    @ApiModelProperty(value = "背景图")
    private String backgroundPic;

    /**
     * 使用类型  0:小程序 1:二维码
     */
    @ApiModelProperty(value = "使用类型  0:小程序 1:二维码")
    private Integer useType;

    /**
     * 小程序码
     */
    @ApiModelProperty(value = "小程序码")
    private String MiniProgramQrCode;

    /**
     * 二维码
     */
    @ApiModelProperty(value = "二维码")
    private String qrCode;

    /**
     * 默认地址
     */
    @ApiModelProperty(value = "默认地址")
    private String Url;

    /**
     * 渠道标记
     */
    @ApiModelProperty(value = "渠道标记")
    private List<String> sources;
}
