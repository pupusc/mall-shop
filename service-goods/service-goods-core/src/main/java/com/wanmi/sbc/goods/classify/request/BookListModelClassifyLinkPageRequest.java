package com.wanmi.sbc.goods.classify.request;

import lombok.Data;

import java.util.Collection;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/17 2:37 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListModelClassifyLinkPageRequest {

    /**
     *   1 排行榜 2 书单 3 编辑推荐 4 专题
     */
    private Collection<Integer> businessTypeList;

    /**
     * 分类id列表
     */
    private Collection<Integer> classifyIdColl;

    private int pageNum = 1;
    private int pageSize = 10;
}
