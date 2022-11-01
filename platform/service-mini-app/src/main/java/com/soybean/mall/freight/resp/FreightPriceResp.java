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
public class FreightPriceResp {

    /**
     * 运费标签信息
     */
    private Label freightLabel;


    /**
     * 标签信息
     */
    @Data
    public static class Label {
        /**
         * 标签名
         */
        private String labelName;

        /**
         * 标签类别 {@link com.soybean.elastic.api.enums.SearchSpuNewLabelCategoryEnum}
         */
        private Integer labelCategory;

        /**
         * 运费
         */
        private String deliveryPrice;
    }
}
