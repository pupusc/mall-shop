package com.wanmi.sbc.goods.api.response.index;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.bean.enums.PublishState;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private PublishState publishState;

}
