package com.wanmi.sbc.goods.api.request.booklistmodel;

import com.wanmi.sbc.goods.api.request.chooserulegoodslist.ChooseRuleGoodsListProviderRequest;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/5 2:03 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/

@Data
public class BookListMixProviderRequest implements Serializable {

    @NotBlank(message = "操作人为空")
    private String operator;

    /**
     * 书单模版
     */
    @Valid
    @NotNull
    private BookListModelProviderRequest bookListModel;

    /**
     * 控件
     */
    @Valid
    @NotNull
    private ChooseRuleGoodsListProviderRequest chooseRuleGoodsListModel;

}
