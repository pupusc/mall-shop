package com.wanmi.sbc.goods.chooserule.request;

import lombok.Data;

import java.util.Collection;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/10 2:04 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ChooseRuleBookListModeIdListRequest {


    private Collection<Integer> bookListIdCollection;

    private Integer category;
}
