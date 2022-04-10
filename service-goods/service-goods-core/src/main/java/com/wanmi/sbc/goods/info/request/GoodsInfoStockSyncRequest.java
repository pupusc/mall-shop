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
public class GoodsInfoStockSyncRequest {

    /**
     * spuId
     */
    private String spuId;

    /**
     * spuNo
     */
    private String spuNo;

    /**
     * erpSpuCode
     */
    private String erpSpuCode;

    /**
     * erpSkuCode
     */
    private String erpSkuCode;

    /**
     * true 表示 直接使用库存传递的库存数量
     * false 表示 不需要重制 计算真实库存
     */
    private Boolean isCalculateStock;

    /**
     * 库存数量
     */
    private Integer erpStockQty;

    /**
     * 成本价
     */
    private BigDecimal erpCostPrice;
}