package com.wanmi.sbc.goods.api.response.goods;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/3/25 1:22 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class GoodsInfoStockSyncMaxIdProviderResponse implements Serializable {

    /**
     * sku
     */
    private List<GoodsInfoStockSyncProviderResponse> goodsInfoStockSyncList;

    private long maxTmpId;
}
