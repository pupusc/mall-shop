package com.wanmi.sbc.goods.api.request.goodsevaluate;

import lombok.Data;

@Data
public class BookFriendEvaluateEditRequest {

    /**
     * 书友说id
     */
    public String evaluateId;
    /**
     * 商品
     */
    public String skuId;

    /**
     * 评价时间
     */
    public String evaluateTime;

    /**
     * 发表人
     */
    public String customerName;

    /**
     * 评价内容
     */
    public String evaluateContent;

    /**
     * 是否展示
     */
    public Integer isShow;

    /**
     * 是否推荐
     */
    public Integer isRecommend;

    /**
     * 店铺id
     */
    public Long storeId;

}
