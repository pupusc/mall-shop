package com.wanmi.sbc.goods.classify.request;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/5 7:38 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListModelClassifyRelRequest {

    /**
     * 书单id
     */
    private Integer bookListModelId;

    /**
     * 类目列表
     */
    private List<Integer> classifyIdList;
}
