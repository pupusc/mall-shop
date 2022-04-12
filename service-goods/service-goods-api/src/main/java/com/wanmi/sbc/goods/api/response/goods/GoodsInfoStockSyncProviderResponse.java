package com.wanmi.sbc.goods.api.response.goods;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/3/25 1:22 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class GoodsInfoStockSyncProviderResponse implements Serializable {

    /**
     * spuId
     */
    private String spuId;

    /**
     * skuId
     */
    private String skuId;

    /**
     * erpSpuCode
     */
    private String erpSpuCode;

    /**
     * erpSkuCode
     */
    private String erpSkuCode;

    /**
     * skuNo
     */
    private String skuNo;

    /**
     * skuNo
     */
    private String skuName;

    /**
     * 是否同步库存
     */
    private boolean canSyncStock = false;

    /**
     * true 表示 直接使用库存传递的库存数量
     * false 表示 不需要重制 计算真实库存
     */
    private Boolean isCalculateStock;

    /**
     * 最终库存数量
     */
    private Integer actualStockQty;

    /**
     * erp库存数量
     */
    private Integer erpStockQty;

    /**
     * 当前库存
     */
    private Integer currentStockQty;

    /**
     * 当前库存冻结是否变更
     */
    private boolean isChangeStock = false;

    /**
     * 是否发送库存消息
     */
    private Boolean isSendStockMsg;

    /**
     * 是否同步成本价
     */
    private boolean canSyncCostPrice = false;

    /**
     *  erp成本价
     */
    private BigDecimal erpCostPrice;

    /**
     *  市场价
     */
    private BigDecimal currentMarketPrice;

    /**
     * 当前成本价
     */
    private BigDecimal currentCostPrice;

}
