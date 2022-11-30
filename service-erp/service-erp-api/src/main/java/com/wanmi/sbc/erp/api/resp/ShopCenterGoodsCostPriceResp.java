package com.wanmi.sbc.erp.api.resp;

import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/11/23 6:28 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ShopCenterGoodsCostPriceResp {

    /**
     * 商品编号
     */
    private String goodsCode;

    /**
     * 店铺编号
     */
    private String shopCode;

    /**
     * 成本价
     */
    private Integer costPrice = 0;
}
