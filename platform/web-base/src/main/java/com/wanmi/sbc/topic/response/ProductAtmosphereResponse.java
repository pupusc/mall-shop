package com.wanmi.sbc.topic.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProductAtmosphereResponse implements Serializable {
    private static final long serialVersionUID = -7522874691287822311L;
    /**
     * skuId
     */
    private String skuId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 丝带第一行文字
     */
    private String title;
    /**
     * 丝带第二行文字
     */
    private String content;
    /**
     * 价格
     */
    private String price;

    /**
     * 图片地址
     */
    private String imageUrl;
}
