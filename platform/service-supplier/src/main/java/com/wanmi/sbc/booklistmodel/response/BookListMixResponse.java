package com.wanmi.sbc.booklistmodel.response;

import com.wanmi.sbc.classify.response.ClassifySimpleResponse;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/6 3:50 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListMixResponse {

    /**
     * 书单模版
     */
    private BookListModelResponse bookListModel;

    /**
     * 类目列表
     */
    private List<ClassifySimpleResponse> classifyList;

    /**
     * 控件
     */
    private ChooseRuleResponse chooseRuleMode;
}
