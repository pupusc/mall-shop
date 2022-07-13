package com.soybean.mall.goods.response.sputopic;


import lombok.Data;

/**
 * 首页主副标题信息
 */
@Data
public class HomeTitleResp {

    /**
     * CODE
     */
    private String code;

    /**
     * 主标题
     */
    private String title;

    /**
     * 副标题
     */
    private String subTitle;

    /**
     * 书单id
     */
    private Integer bookListModelId;
}