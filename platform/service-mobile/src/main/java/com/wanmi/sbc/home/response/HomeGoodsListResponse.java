package com.wanmi.sbc.home.response;

import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/26 1:54 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class HomeGoodsListResponse {

    /**
     * 新上
     */
    private List<GoodsCustomResponse> newBookGoodsList;

    /**
     * 畅销
     */
    private List<GoodsCustomResponse>  sellWellGoodsList;


    /**
     * 特价书
     */
    private List<GoodsCustomResponse>  specialOfferBookList;
}
