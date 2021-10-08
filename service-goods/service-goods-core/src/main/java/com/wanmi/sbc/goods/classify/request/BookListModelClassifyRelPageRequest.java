package com.wanmi.sbc.goods.classify.request;

import lombok.Data;

import java.util.Collection;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/17 1:35 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListModelClassifyRelPageRequest {

    private int pageSize= 10;

    private int pageNum = 1;

    /**
     * 分类id列表
     */
    private Collection<Integer> classifyIdColl;

    /**
     * 书单id
     */
    private Integer bookListModelId;

}
