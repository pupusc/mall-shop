package com.wanmi.sbc.booklistmodel.response;

import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/13 4:53 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListModelAndGoodsListResponse {

    /**
     * 书单模板对象
     */
    private BookListModelProviderResponse bookListModel;

    /**
     * 商品列表
     */
    private List<GoodsCustomResponse> goodsList;
}
