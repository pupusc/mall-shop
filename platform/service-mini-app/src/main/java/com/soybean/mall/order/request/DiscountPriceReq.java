package com.soybean.mall.order.request;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/30 12:55 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class DiscountPriceReq {


    /**
     * 商品信息
     */
    private List<DiscountPriceSkuReq> items;

    /**
     * 折扣信息
     */
    private List<DiscountMarketingSkuReq> marketings;


    @Data
    public static class DiscountPriceSkuReq {


        /**
         * skuId
         */
        private String skuId;

        /**
         * 数量
         */
        private Integer num;
    }


    @Data
    public static class DiscountMarketingSkuReq {
        private Long marketingId;

        private Long marketingLevelId;

        private List<String> skuIds;
    }
}