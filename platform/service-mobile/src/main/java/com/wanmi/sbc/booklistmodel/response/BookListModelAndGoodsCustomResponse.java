package com.wanmi.sbc.booklistmodel.response;

import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/18 2:35 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListModelAndGoodsCustomResponse {

    /**
     * 书单信息 
     */
    private BookListModelSimpleResponse bookListModel;

    /**
     * 商品对象
     */
    private GoodsCustomResponse goodsCustomVo;
}
