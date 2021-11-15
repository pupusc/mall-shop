package com.wanmi.sbc.booklistmodel.response;

import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/11/10 1:14 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListModelAndGoodsCountResponse extends BookListModelProviderResponse {

    /**
     * 商品数量
     */
    private Integer goodsCount;
}
