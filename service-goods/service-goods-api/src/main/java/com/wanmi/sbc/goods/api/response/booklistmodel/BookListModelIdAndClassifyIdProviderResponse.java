package com.wanmi.sbc.goods.api.response.booklistmodel;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/17 2:03 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListModelIdAndClassifyIdProviderResponse implements Serializable {


    /**
     * 书单id
     */
    private Integer bookListModelId;

    /**
     * 书单模版名
     */
    private String name;

    /**
     * 分类id
     */
    private Integer classifyId;

    /**
     * 父分类
     */
    private Integer classifyParentId;

    /**
     * 分类名字
     */
    private String classifyName;
}
