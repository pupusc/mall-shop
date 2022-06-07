package com.wanmi.sbc.goods.response;

import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/6 7:55 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class GoodsInfoStockSyncRes {

    /**
     * 可用库存
     */
    private Long stock;

    /**
     * 总库存数量
     */
    private Long totalStock;

    /**
     * 冻结库存数量
     */
    private Long freezeStock;

    /**
     * 成本价
     */
    private String costPrice;
}
