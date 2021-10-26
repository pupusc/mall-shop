package com.wanmi.sbc.home.response;

import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/25 2:15 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class HomeBookListRecommendResponse {
    /**
     * 书单推荐
     */
    private List<BookListModelProviderResponse> bookListModelRecommend;

    /**
     * 名人推荐
     */
    private List<BookListModelProviderResponse> famousRecommend;
}
