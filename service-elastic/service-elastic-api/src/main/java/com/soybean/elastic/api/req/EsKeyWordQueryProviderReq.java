package com.soybean.elastic.api.req;

import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 1:46 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class EsKeyWordQueryProviderReq extends EsCommonPageQueryReq {

    /**
     * 关键字
     */
    private String keyword;
}
