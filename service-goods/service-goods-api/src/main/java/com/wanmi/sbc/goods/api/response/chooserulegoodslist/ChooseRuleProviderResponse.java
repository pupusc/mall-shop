package com.wanmi.sbc.goods.api.response.chooserulegoodslist;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/5 1:23 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ChooseRuleProviderResponse implements Serializable {

    private Integer chooseRuleId;

    private Integer bookListId;

    private Integer category;

    private Integer filterRule;

    private Integer chooseType;

    private String chooseCondition;

    private Date createTime;

    private Date updateTime;

    private List<BookListGoodsProviderResponse> bookListGoodsList;
}
