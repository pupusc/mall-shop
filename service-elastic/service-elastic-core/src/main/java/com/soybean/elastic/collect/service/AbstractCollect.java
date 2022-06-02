package com.soybean.elastic.collect.service;


import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public abstract class AbstractCollect {


    /**
     * 采集商品id
     * @param lastCollectTime
     * @param now
     * @param <F>
     * @return
     */
    public abstract <F> Set<F> collectId(LocalDateTime lastCollectTime, LocalDateTime now);


    /**
     * 采集信息
     * @return
     */
    public abstract <F> List<F> collect(List<F> list);




    private long beforeCollect() {
        return System.currentTimeMillis();
    }





    /**
     * 初始化增量数据
     * @param <F>
     * @return
     */
    public <F> Set<F> incrementalLoadSpuId(LocalDateTime lastCollectTime, LocalDateTime now) {
        long beginTime = beforeCollect();
        Set<F> result = collectId(lastCollectTime, now);
        afterCollect(beginTime, "collectSpuId");
        return result;
    }

    /**
     * 初始化增量数据
     * @return
     */
    public <T> List<T> incrementalLoads(List<T> list) {
        long beginTime = beforeCollect();
        List<T> collect = collect(list);
        afterCollect(beginTime, "collect");
        return collect;
    }


    private void afterCollect(Long beginTime,String methodName){
        log.info("AbstractSpuCollect {} methodName:{} cost: {}", this.getClass().getSimpleName(), methodName, (System.currentTimeMillis() - beginTime) / 1000);
    }




}
