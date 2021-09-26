package com.wanmi.sbc.index.response;

import lombok.Data;

import java.io.Serializable;


@Data
public class IndexConfigChild1Response implements Serializable {

    private static final long serialVersionUID = 9141263828409602407L;
    /**
     * 图片地址
     */
    private String imgUrl;

    /**
     * 跳转地址
     */
    private String jumpUrl;

    /**
     * 跳转类型 1-ID站内跳转  2-url跳转
     */
    private Integer type = 1;
    /**
     * 跳转ID
     */
    private String id;
}