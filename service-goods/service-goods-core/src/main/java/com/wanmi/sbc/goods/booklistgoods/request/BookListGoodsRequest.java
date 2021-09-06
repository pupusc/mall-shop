package com.wanmi.sbc.goods.booklistgoods.request;

import com.wanmi.sbc.goods.api.request.booklistmodel.GoodsIdListProviderRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/2 2:49 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListGoodsRequest implements Serializable {

    /**
     * 控件id
     */
    private Integer chooseRuleId;

    private Integer bookListId;

    private Integer category;

    /**
     * goodIdList
     */
    private List<GoodsIdListProviderRequest> goodsIdListRequestList;
}
