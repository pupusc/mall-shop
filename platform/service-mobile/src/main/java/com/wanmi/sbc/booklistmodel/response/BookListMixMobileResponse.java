package com.wanmi.sbc.booklistmodel.response;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/15 7:01 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListMixMobileResponse {

    /**
     * 书单模版
     */
    private BookListModelMobileResponse bookListModel;

    /**
     * 商品列表信息
     */
    private List<GoodsCustomResponse> goodsList;
}
