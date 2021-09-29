package com.wanmi.sbc.goods.api.response.booklistmodel;

import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/8 5:15 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListModelAndOrderNumProviderResponse {

    /**
     * 书单id
     */
    private Integer bookListModelId;

    /**
     * 书单名称
     */
    private String bookListModelName;

    /**
     * 排序
     */
    private Integer orderNum;
}
