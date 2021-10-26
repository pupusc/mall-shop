package com.wanmi.sbc.goods.api.request.index;

import com.wanmi.sbc.goods.bean.enums.PublishState;
import lombok.Data;

@Data
public class CmsTitleUpdateRequest {

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
    private PublishState publishState;

    /**
     * 排序
     */
    private Integer orderNum;

}
