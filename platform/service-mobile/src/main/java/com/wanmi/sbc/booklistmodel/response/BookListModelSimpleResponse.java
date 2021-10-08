package com.wanmi.sbc.booklistmodel.response;

import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/18 2:37 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListModelSimpleResponse {

    private Integer id;

    /**
     * 模版名
     */
    private String name;

    /**
     * 描述
     */
    private String desc;

    /**
     * 书单模板类型 1 排行榜 2 书单 3 编辑推荐 4 专题
     */
    private Integer businessType;

    /**
     * 排序
     */
    private Integer orderNum;
}
