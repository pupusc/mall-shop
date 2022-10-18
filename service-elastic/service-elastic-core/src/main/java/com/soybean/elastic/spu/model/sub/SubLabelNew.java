package com.soybean.elastic.spu.model.sub;

import lombok.Data;

/**
 * Description: 标签信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/19 1:16 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SubLabelNew {

    /**
     * 标签名
     */
    private String name;

    /**
     * 1、49包邮标签 {@link com.soybean.elastic.api.enums.SearchSpuNewLabelCategoryEnum}
     */
    private Integer category;
}
