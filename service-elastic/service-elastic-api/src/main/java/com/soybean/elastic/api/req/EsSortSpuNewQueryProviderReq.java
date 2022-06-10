package com.soybean.elastic.api.req;

import com.soybean.common.req.CommonPageQueryReq;
import com.soybean.elastic.api.enums.SearchSpuNewSortTypeEnum;
import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 1:46 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class EsSortSpuNewQueryProviderReq extends CommonPageQueryReq {

    /**
     * 排序类型 0 默认
     */
    private SearchSpuNewSortTypeEnum spuSortType;

}
