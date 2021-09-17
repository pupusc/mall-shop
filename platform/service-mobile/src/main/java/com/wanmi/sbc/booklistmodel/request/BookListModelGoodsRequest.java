package com.wanmi.sbc.booklistmodel.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/16 8:48 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListModelGoodsRequest {

    private int pageNum = 1;

    private int pageSize = 10;

    @NotNull
    private Integer bookListModelId;
}
