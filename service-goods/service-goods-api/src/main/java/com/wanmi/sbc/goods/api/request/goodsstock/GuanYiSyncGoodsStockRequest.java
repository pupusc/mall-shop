package com.wanmi.sbc.goods.api.request.goodsstock;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/3/29 1:21 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class GuanYiSyncGoodsStockRequest implements Serializable {

    private List<String> goodsIdList;

//    private String startTime;

    private long maxTmpId;

    private int pageSize;

    /**
     * 此次是否记录到redis中
     */
    private boolean hasSaveRedis = false;
}
