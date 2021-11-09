package com.wanmi.sbc.goods.booklistgoodspublish.response;

import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/11/9 3:22 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class CountBookListModelGroupResponse {

    /**
     * 书单id中商品数量
     */
    private int goodsCount;

    /**
     * 书单id
     */
    private int bookListModelId;
}
