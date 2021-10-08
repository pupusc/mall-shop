package com.wanmi.sbc.elastic.api.request.goods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.search.sort.SortOrder;

import java.io.Serializable;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/23 2:32 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortCustomBuilder implements Serializable {
    /**
     * 排序字段
     */
    private String fieldName;

    /**
     * 排序
     */
    private SortOrder sortOrder;
}
