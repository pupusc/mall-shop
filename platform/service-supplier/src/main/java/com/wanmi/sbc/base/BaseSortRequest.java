package com.wanmi.sbc.base;

import com.wanmi.sbc.goods.api.request.BaseSortProviderRequest;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/11/17 6:10 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BaseSortRequest {

    /**
     * 排序
     */
    private List<BaseSortProviderRequest> sortList;
}
