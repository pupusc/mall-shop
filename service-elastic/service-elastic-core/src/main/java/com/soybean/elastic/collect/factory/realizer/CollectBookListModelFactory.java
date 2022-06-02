package com.soybean.elastic.collect.factory.realizer;

import com.alibaba.fastjson.JSON;
import com.soybean.elastic.booklistmodel.model.EsBookListModel;
import com.soybean.elastic.collect.factory.AbstractCollectFactory;
import com.soybean.elastic.collect.service.booklistmodel.AbstractBookListModelCollect;
import com.soybean.elastic.collect.service.booklistmodel.service.BookListModelCollect;
import com.soybean.elastic.collect.service.spu.AbstractSpuCollect;
import com.soybean.elastic.collect.service.spu.service.SpuCollect;
import com.soybean.elastic.spu.model.EsSpuNew;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/31 5:45 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CollectBookListModelFactory extends AbstractCollectFactory {


    @Autowired
    private Map<String, AbstractBookListModelCollect> bookListModelCollectMap;


    @Override
    protected EsBookListModel packModelId(Object k) {
        EsBookListModel esBookListModel = new EsBookListModel();
        esBookListModel.setBookListId((long)k);
        return esBookListModel;
    }


    @Override
    protected Class<?> firstLoadCls() {
        return BookListModelCollect.class;
    }

    @PostConstruct
    public void initSpuCollect() {
        LocalDateTime now = LocalDateTime.now();
        int minSize = 50;
        LocalDateTime lastCollectTime = LocalDateTime.of(2021,12,12,12,12,12,12);
        super.load(bookListModelCollectMap, lastCollectTime, now, minSize);
    }


    @Override
    protected <S> void after(List<S> modelList) {
        if (CollectionUtils.isEmpty(modelList)) {
            return;
        }

        for (S s : modelList) {
            System.out.println(JSON.toJSONString(s) + ">>>>>>>>>>>>>>");
        }
    }


//    @PostConstruct
//    public void initSpuCollect() {
////        System.out.println(">>>>>>>>>" + spuCollectMap);
//        LocalDateTime now = LocalDateTime.now();
//        int maxSize = 50;
//        LocalDateTime lastCollectTime = LocalDateTime.of(2021,12,12,12,12,12,12);
//
//        Set<String> spuIdResultSet = new HashSet<>();
//        for (String key : spuCollectMap.keySet()) {
//            AbstractSpuCollect spuCollect = spuCollectMap.get(key);
//            Set<String> spuIdTmpSet = spuCollect.incrementalLoadSpuId(lastCollectTime, now);
//            if (spuIdTmpSet.size() < maxSize) {
//                spuIdResultSet.addAll(spuIdTmpSet);
//                continue;
//            }
//            this.initEs(this.collect(spuIdResultSet));
//            spuIdResultSet.clear();
//        }
//
//        if (!CollectionUtils.isEmpty(spuIdResultSet)) {
//            this.initEs(this.collect(spuIdResultSet));
//        }
//
//        //更新es TODO
//    }
//
//
//    /**
//     * 获取采集结果
//     * @param spuIdResultSet
//     * @return
//     */
//    private List<EsSpuNew> collect(Set<String> spuIdResultSet) {
//
//        List<EsSpuNew> result = new ArrayList<>();
//        if (CollectionUtils.isEmpty(spuIdResultSet)) {
//            return result;
//        }
//
//        List<EsSpuNew> spuIdList = new ArrayList<>();
//        for (String s : spuIdResultSet) {
//            EsSpuNew esSpuNew = new EsSpuNew();
//            esSpuNew.setSpuId(s);
//            spuIdList.add(esSpuNew);
//        }
//
//        for (String keyChild : spuCollectMap.keySet()) {
//            //采集数据
//            AbstractSpuCollect collectChild = spuCollectMap.get(keyChild);
//            if (collectChild instanceof SpuCollect) {
//                result.addAll(collectChild.incrementalLoads(spuIdList));
//                break;
//            }
//        }
//
//        if (!CollectionUtils.isEmpty(result)) {
//            for (String keyChild : spuCollectMap.keySet()) {
//                //采集数据
//                AbstractSpuCollect collectChild = spuCollectMap.get(keyChild);
//                if (collectChild instanceof SpuCollect) {
//                    continue;
//                }
//                result.addAll(collectChild.incrementalLoads(spuIdList));
//            }
//        }
//        return result;
//    }


//    /**
//     * 采集结果初始化到es
//     * @param esSpuNewList
//     */
//    private void initEs(List<EsSpuNew> esSpuNewList) {
//        if (CollectionUtils.isEmpty(esSpuNewList)) {
//            return;
//        }
//
//        for (EsSpuNew esSpuNew : esSpuNewList) {
//            System.out.println(JSON.toJSONString(esSpuNew) + "<<<<<<<<<<<<<<<<<,");
//        }
//    }


//
//    @PostConstruct
//    public void init() {
//        List<EsSpuNew> init = spuCollect.init();
//        System.out.println(init);
//    }
}
