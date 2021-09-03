package com.wanmi.sbc.goods.api.request.booklistgoods;

import lombok.Data;

import javax.validation.constraints.NotNull;
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
public class BookListGoodsProviderRequest implements Serializable {

    /**
     * 控件id
     */
    @NotNull
    private Integer chooseRuleId;

    /**
     * goodIdList
     */
    @NotNull
    private List<GoodsIdListProviderRequest> goodsIdListProviderRequestList;
}
