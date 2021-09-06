package com.wanmi.sbc.booklistmodel.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/6 10:34 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListMixRequest {


    /**
     * 书单模版
     */
    @Valid
    @NotNull
    private BookListModelRequest bookListModel;

    /**
     * 控件模版
     */
    @Valid
    private ChooseRuleGoodsListRequest chooseRuleGoodsListModel;
}
