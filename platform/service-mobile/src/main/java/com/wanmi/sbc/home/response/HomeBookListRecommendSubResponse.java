package com.wanmi.sbc.home.response;

import com.wanmi.sbc.booklistmodel.response.BookListModelAndGoodsCountResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/26 2:34 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class HomeBookListRecommendSubResponse {

    /**
     * 主副标题
     */
    private HomeTopicResponse homeTopicResponse;

    /**
     * 推荐
     */
    private List<BookListModelAndGoodsCountResponse> recommendList;
}
