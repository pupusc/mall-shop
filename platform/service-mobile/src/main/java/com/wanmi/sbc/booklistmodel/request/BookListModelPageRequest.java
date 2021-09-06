package com.wanmi.sbc.booklistmodel.request;

import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/6 10:43 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListModelPageRequest {
    private int pageNum = 1;

    private int pageSize = 10;

    private Integer id;

    /**
     * 名字
     */
    private String name;

    /**
     * 发布状态 0 草稿 1 已编辑未发布 2 已发布
     */
    private Integer publishState;
}
