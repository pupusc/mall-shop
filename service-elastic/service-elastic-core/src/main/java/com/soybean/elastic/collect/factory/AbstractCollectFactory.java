package com.soybean.elastic.collect.factory;

import com.soybean.elastic.api.req.collect.CollectJobReq;
import com.soybean.elastic.collect.service.AbstractCollect;
import com.soybean.elastic.collect.service.spu.AbstractSpuCollect;
import com.soybean.elastic.collect.service.spu.service.SpuCollect;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/31 5:45 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public abstract class AbstractCollectFactory {

    public static final String INDEX_ES_BOOK_LIST_MODEL = "es_book_list_model";

    public static final String INDEX_ES_SPU_NEW = "es_spu_new";

    public static final Integer MAX_SIZE = 10000;

    /**
     * 根据key生成要拼装的对象信息
     * @param <S>
     * @return
     */
    protected abstract <S> S packModelId(Object key);

    /**
     * 该类优先加载, 如果不指定则无序加载
     * @return
     */
    protected Class<?> firstLoadCls(){
        return Object.class;
    }


    /**
     * 对外提供方法
     */
    public abstract void init(CollectJobReq collectJobReq);

    /**
     * 采集完数据 后续处理
     * @param modelList
     * @param now
     * @param <S>
     */
    protected abstract <S> void after(List<S> modelList, LocalDateTime now);


    /**
     * 全部执行完毕后调用
     * @param
     */
    protected abstract void finalized(LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 限量采集数据
     * @param keySet
     * @return
     */
    private <S> List<S> bulkCollect(Set<Object> keySet, Map<String, ? extends AbstractCollect> collectMap) {

        List<S> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(keySet)) {
            return result;
        }

        for (Object s : keySet) {
            result.add(this.packModelId(s));
        }

        for (String keyChild : collectMap.keySet()) {
            //采集数据
            AbstractCollect collectChild = collectMap.get(keyChild);
            if (collectChild.getClass() == this.firstLoadCls()) {
                collectChild.incrementalLoads(result);
                break;
            }
        }

        if (!CollectionUtils.isEmpty(result)) {
            for (String keyChild : collectMap.keySet()) {
                //采集数据
                AbstractCollect collectChild = collectMap.get(keyChild);
                if (collectChild.getClass() == this.firstLoadCls()) {
                    continue;
                }
                collectChild.incrementalLoads(result);
            }
        }
        return result;
    }

    /**
     *
     * @param collectMap  实现类列表
     * @param lastCollectTime 上一次存储时间
     * @param now 当前时间
     * @param minSize 最小处理数量[只要超过该值就会调用 after 方法]
     */
    protected  void load(Map<String, ? extends AbstractCollect> collectMap, LocalDateTime lastCollectTime, LocalDateTime now, Integer minSize){
        this.load(collectMap, lastCollectTime, now, minSize, true);
    }

    /**
     *
     * @param collectMap  实现类列表
     * @param lastCollectTime 上一次存储时间
     * @param now 当前时间
     * @param minSize 最小处理数量[只要超过该值就会调用 after 方法]
     */
    protected  void load(Map<String, ? extends AbstractCollect> collectMap, LocalDateTime lastCollectTime, LocalDateTime now, Integer minSize, boolean hasRunFinalize) {

        Set<Object> spuIdResultSet = new HashSet<>();
        for (String key : collectMap.keySet()) {
            AbstractCollect spuCollect = collectMap.get(key);
            Set<Object> spuIdTmpSet = spuCollect.incrementalLoadSpuId(lastCollectTime, now);
            spuIdResultSet.addAll(spuIdTmpSet);
            if (spuIdTmpSet.size() < minSize) {
                continue;
            }
            this.after(bulkCollect(spuIdResultSet, collectMap), now);
            spuIdResultSet.clear();
        }

        if (!CollectionUtils.isEmpty(spuIdResultSet)) {
            this.after(bulkCollect(spuIdResultSet, collectMap), now);
        }

        if (hasRunFinalize) {
            this.finalized(lastCollectTime, now);
        }
    }



}
