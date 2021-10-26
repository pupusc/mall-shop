package com.wanmi.sbc.goods.api.response.index;

import lombok.Data;

@Data
public class IndexModuleVo {

    private Integer id;

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
     * 启用状态
     */
    private Integer publishState;

}
