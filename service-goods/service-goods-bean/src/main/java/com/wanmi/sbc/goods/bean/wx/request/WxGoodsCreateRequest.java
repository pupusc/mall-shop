package com.wanmi.sbc.goods.bean.wx.request;

import lombok.Data;

import javax.persistence.Column;
import java.util.Map;

@Data
public class WxGoodsCreateRequest {

    private Long id;

    /**
     * spu id
     */
    private String goodsId;

    /**
     * isbn图片
     */
    private String isbnImg;

    /**
     * 出版社图片
     */
    private String publisherImg;

    /**
     * 微信类目
     */
    private Map<String, String> wxCategory;
}
