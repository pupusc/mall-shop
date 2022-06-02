package com.soybean.elastic.collect.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collection;
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

    public static final Integer MAX_PAGE_SIZE = 500;

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
        afterCollect(beginTime, "collectId");
        return result;
    }

    /**
     * 初始化增量数据
     * @return
     */
    public <T> void incrementalLoads(List<T> list) {
        long beginTime = beforeCollect();
        int from = 0;
        while (list.size() >= AbstractCollect.MAX_PAGE_SIZE) {
            collect(list.subList(from, from + AbstractCollect.MAX_PAGE_SIZE));
            from += AbstractCollect.MAX_PAGE_SIZE;
        }

        List<T> ts = list.subList(from, list.size());
        if (!CollectionUtils.isEmpty(ts)) {
            collect(ts);
        }

        afterCollect(beginTime, "collect");
//        return null;
    }


    private void afterCollect(Long beginTime,String methodName){
        log.info("AbstractSpuCollect {} methodName:{} cost: {}", this.getClass().getSimpleName(), methodName, (System.currentTimeMillis() - beginTime) / 1000);
    }




}
