package com.wanmi.sbc.classify.response;

import com.wanmi.sbc.booklistmodel.response.BookListModelMobileResponse;
import com.wanmi.sbc.booklistmodel.response.BookListModelSimpleResponse;
import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import lombok.Data;

import java.util.List;

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
     * 商品列表信息
     */
    private List<GoodsCustomResponse> goodsCustomResponseList;

    /**
     * 书单信息
     */
    private BookListModelMobileResponse bookListModelMobile;
}
