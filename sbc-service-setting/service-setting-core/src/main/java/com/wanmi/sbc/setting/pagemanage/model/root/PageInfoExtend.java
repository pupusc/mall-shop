package com.wanmi.sbc.setting.pagemanage.model.root;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

/**
 * 物流记录
 * Created by dyt on 2020/4/17.
 */
@Data
public class PageInfoExtend implements Serializable {

    private static final long serialVersionUID = -6414564526969459575L;

    /**
     * pageId
     */
    @Id
    private String pageId;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 页面code
     */
    private String pageCode;

    /**
     * 页面类型
     */
    private String pageType;

    /**
     * 页面所属平台
     */
    private String platform;

    /**
     * 背景图
     */
    private String backgroundPic;

    /**
     * 使用类型  0:小程序 1:二维码
     */
    private Integer useType;

    /**
     * 小程序码
     */
    private String MiniProgramQrCode;

    /**
     * 二维码
     */
    private String qrCode;

    /**
     * 访问地址
     */
    private String url;

    /**
     * 来源
     */
    private List<String> sources;
}
