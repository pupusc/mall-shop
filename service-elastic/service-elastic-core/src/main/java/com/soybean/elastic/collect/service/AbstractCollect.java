package com.soybean.elastic.collect.service;


import com.soybean.elastic.spu.model.EsSpuNew;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/1 2:51 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public abstract class AbstractCollect {


    /**
     * 采集商品id
     * @param lastCollectTime
     * @param now
     * @param <F>
     * @return
     */
    public abstract <F> Set<F> collectSpuId(LocalDateTime lastCollectTime, LocalDateTime now);


    /**
     * 采集信息
     * @return
     */
    public abstract <F> List<F> collect(List<F> list);

}
