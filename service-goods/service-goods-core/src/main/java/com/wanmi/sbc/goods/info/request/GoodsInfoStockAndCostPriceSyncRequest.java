package com.wanmi.sbc.goods.info.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/3/24 1:26 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/

@Data
public class GoodsInfoStockAndCostPriceSyncRequest {
    /**
     *
     */
    private String goodsId;

    /**
     *
     */
    private String goodsInfoId;

    private String goodsInfoNo;

    private String goodsInfoName;

    private String erpGoodsNo;

    private String erpGoodsInfoNo;

    /**
     * 成本价
     */
    private BigDecimal costPrice;

    /**
     * 市场价
     */
    private BigDecimal marketPrice;
    /**
     * 成本价同步标记
     */
    private Integer costPriceSyncFlag;

    /**
     * 库存数量
     */
    private Long stockQTY;

    /**
     * 库存同步标记
     */
    private Integer stockSyncFlag;

    /**
     * 此次是否记录到redis中
     */
    private boolean hasSaveRedis = false;

    /**
     * 1001=库存变动，1004=成本价变动
     */
    private Integer tag;

    /**
     * 数量
     */
    private Integer quantity = 0;
}
