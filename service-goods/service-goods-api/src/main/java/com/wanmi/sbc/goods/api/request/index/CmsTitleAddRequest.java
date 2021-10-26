package com.wanmi.sbc.goods.api.request.index;

import lombok.Data;

@Data
public class CmsTitleAddRequest {

    /**
     * CODE
     */
    private String code;

    /**
     * 主标题
     */
    private String title;

    /**
     * 副标题
     */
    private String subTitle;

    /**
     * 书单id
     */
    private Integer bookListModelId;

    /**
     * 排序
     */
    private Integer orderNum;

}
