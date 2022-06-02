package com.soybean.elastic.collect.factory;

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

    /**
     * 打包类信息
     * @param k
     * @param <S>
     * @return
     */
    protected abstract <S> S packModel(String k);

    /**
     * 过滤类信息
     * @return
     */
    protected abstract Class<?> firstLoadCls();

    /**
     * 获取采集结果
     * @param keySet
     * @return
     */
    private <S> List<S> bulkCollect(Set<String> keySet, Map<String, ? extends AbstractCollect> collectMap) {

        List<S> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(keySet)) {
            return result;
        }

        List<S> spuIdList = new ArrayList<>();
        for (String s : keySet) {
            spuIdList.add(this.packModel(s));
        }

        for (String keyChild : collectMap.keySet()) {
            //采集数据
            AbstractCollect collectChild = collectMap.get(keyChild);
            if (collectChild.getClass() == this.firstLoadCls()) {
                result.addAll(collectChild.incrementalLoads(spuIdList));
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
                result.addAll(collectChild.incrementalLoads(spuIdList));
            }
        }
        return result;
    }


//    protected abstract before();

//    protected  <S> void load(Set<String> keySet, Map<String, ? extends AbstractCollect> collectMap) {
//        this.after(bulkCollect(keySet, collectMap));
//    }

    protected  <S> void load(Map<String, ? extends AbstractCollect> collectMap, LocalDateTime lastCollectTime, LocalDateTime now, Integer minSize) {

        Set<String> spuIdResultSet = new HashSet<>();
        for (String key : collectMap.keySet()) {
            AbstractCollect spuCollect = collectMap.get(key);
            Set<String> spuIdTmpSet = spuCollect.incrementalLoadSpuId(lastCollectTime, now);
            if (spuIdTmpSet.size() < minSize) {
                spuIdResultSet.addAll(spuIdTmpSet);
                continue;
            }
            this.after(bulkCollect(spuIdResultSet, collectMap));
            spuIdResultSet.clear();
        }

        if (!CollectionUtils.isEmpty(spuIdResultSet)) {
            this.after(bulkCollect(spuIdResultSet, collectMap));
        }
    }

    protected abstract <S> void after(List<S> modelList);

}
