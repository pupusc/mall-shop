package com.wanmi.sbc.booklistmodel.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/16 9:09 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListModelPageRequest {

    private int pageNum = 1;

    private int pageSize = 10;

    /**
     * 书单模板类型 1 排行榜 2 书单 3 编辑推荐 4 专题 5 名家推荐
     */
    @NotNull
    private Integer businessType;


}
