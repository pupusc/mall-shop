package com.wanmi.sbc.goods.info.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/27 1:22 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ErpGoodsInfoRequest {


    /**
     * 成本价
     */
    private BigDecimal erpCostPrice;

    /**
     * 是否需要同步成本价
     */
    private Boolean hasSyncCostPrice = false;

    /**
     * erp库存
     */
    private Long erpStock;

    /**
     * 是否需要同步库存
     */
    private Boolean hasSyncStock = false;
}
