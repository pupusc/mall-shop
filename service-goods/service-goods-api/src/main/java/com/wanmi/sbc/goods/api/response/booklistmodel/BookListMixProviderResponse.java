package com.wanmi.sbc.goods.api.response.booklistmodel;

import com.wanmi.sbc.goods.api.response.chooserulegoodslist.ChooseRuleProviderResponse;
import com.wanmi.sbc.goods.api.response.classify.ClassifySimpleProviderResponse;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/1 6:04 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListMixProviderResponse implements Serializable {


    private BookListModelProviderResponse bookListModel;

    /**
     * 类目
     */
    private List<ClassifySimpleProviderResponse> classifyList;

    private ChooseRuleProviderResponse chooseRuleMode;
    
}
