package com.soybean.elastic.api.resp;

import com.soybean.common.resp.CommonPageResp;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 聚合结果
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/20 2:45 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class EsSpuNewAggResp<S> implements Serializable {

    /**
     * 聚合 标签 信息
     */
    private List<String> labels;

    /**
     * 结果对象信息
     */
    private CommonPageResp<S> result;
}
