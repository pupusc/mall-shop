package com.wanmi.sbc.booklistmodel.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/18 1:44 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
@NoArgsConstructor
public class RankingPageRequest {

    /**
     * spuId
     */
    @NonNull
    private String spuId;

    private int pageNum = 0;

    private int pageSize = 10;
}
