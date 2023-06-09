package com.soybean.mall.freight.req;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/22 12:58 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class FreightPriceListReq {

    private List<FreightSkuReq> skus;

    /**
     * 省份id
     */
    private String provinceId;

    /**
     * 市id
     */
    private String cityId;

    /**
     * 折扣信息
     */
    private List<DiscountMarketingSkuReq> marketings;


    @Data
    public static class FreightSkuReq {
        private String skuId;

        private Integer num;
    }

    @Data
    public static class DiscountMarketingSkuReq {
        private Long marketingId;

        private Long marketingLevelId;

        private List<String> skuIds;
    }
}
