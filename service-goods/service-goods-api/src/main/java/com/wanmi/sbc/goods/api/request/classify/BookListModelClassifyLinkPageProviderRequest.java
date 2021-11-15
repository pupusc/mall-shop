package com.wanmi.sbc.goods.api.request.classify;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

@Data
public class BookListModelClassifyLinkPageProviderRequest implements Serializable {

    /**
     *   1 排行榜 2 书单 3 编辑推荐 4 专题
     */
    private Collection<Integer> businessTypeList;

    private Collection<Integer> unShowBookListModelIdList;

    /**
     * 分类id列表
     */
    private Collection<Integer> classifyIdColl;

    private int pageNum = 1;
    private int pageSize = 10;
}