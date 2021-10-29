package com.wanmi.sbc.classify.response;

import com.wanmi.sbc.booklistmodel.response.BookListModelAndGoodsCustomResponse;
import com.wanmi.sbc.booklistmodel.response.BookListModelSimpleResponse;
import lombok.Data;


/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/21 3:24 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ClassifyGoodsAndBookListModelResponse {

    /**
     * 1表示 商品 2表示 书单
     */
    private Integer type;

    /**
     * 商品列表信息
     */
    private BookListModelAndGoodsCustomResponse bookListModelAndGoodsCustomModel;

    /**
     * 书单信息
     */
    private BookListModelSimpleResponse bookListModel;
}
