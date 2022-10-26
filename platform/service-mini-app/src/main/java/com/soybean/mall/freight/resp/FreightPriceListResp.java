package com.soybean.mall.freight.resp;

import lombok.Data;



/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/22 1:20 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class FreightPriceListResp {

    /**
     * 运费
     */
    private String deliveryPrice;

    /**
     * 是否包含49包邮
     */
    private boolean hasFreeDelivery = false;

    /**
     * 差多少钱可以49包邮
     */
    private String diffFreeDelivery;

//    /**
//     * 计算运费的商品
//     */
//    private List<FreightSku> freightSkus;
//
//    @Data
//    public static class FreightSku {
//
//        private String skuId;
//
//        /**
//         * 标签名
//         */
//        private String labelName;
//
//        /**
//         * 标签类别 {@link com.soybean.elastic.api.enums.SearchSpuNewLabelCategoryEnum} 0 无 1 49包邮
//         */
//        private Integer labelCategory;
//    }
}
