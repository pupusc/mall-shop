package com.wanmi.sbc.goods.api.response.collect;

import lombok.Data;

import java.util.Date;

/**
 * Description: 采集店铺分类
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/14 2:27 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class CollectClassifyRelSpuDetailResp extends CollectClassifyRelSpuResp{

    /**
     * 店铺分类名称
     */
    private String classifyName;

    /**
     * 1级店铺分类id
     */
    private Integer fClassifyId;

    /**
     * 1级别店铺分类名称
     */
    private String fClasssifyName;
}
