package com.wanmi.sbc.elastic.common;

import lombok.Builder;
import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/3/11 2:54 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
//@Builder
public class CommonEsSearchCriteriaRequest {

    /**
     * 获取源头 1 H5 2 小程序 3 内购
     */
    private Integer channelType;
}
