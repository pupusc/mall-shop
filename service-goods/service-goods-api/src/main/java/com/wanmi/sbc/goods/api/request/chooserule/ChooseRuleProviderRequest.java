package com.wanmi.sbc.goods.api.request.chooserule;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/22 6:14 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ChooseRuleProviderRequest implements Serializable {

    /**
     * 控件id
     */
    private Integer chooseRuleId;

}
