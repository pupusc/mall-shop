package com.wanmi.sbc.bookmeta.bo.collect;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/17 1:08 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class CollectMetaResp {

    private int lastBizId = 0;

    /**
     * 是否还有数据
     */
    private boolean hasMore = false;

    private List<CollectMetaBookResp> metaBookResps;

    /**
     * 图书信息
     */
    public static class CollectMetaBookResp {

    }
}
