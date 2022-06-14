package com.soybean.elastic.collect.factory.realizer;

import com.alibaba.fastjson.JSON;
import com.soybean.elastic.collect.factory.AbstractCollectFactory;
import com.soybean.elastic.collect.service.spu.AbstractSpuCollect;
import com.soybean.elastic.collect.service.spu.service.SpuCollect;
import com.soybean.elastic.spu.model.EsSpuNew;
import com.soybean.elastic.spu.repository.EsSpuNewRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class CollectSpuFactory extends AbstractCollectFactory {


    @Autowired
    private Map<String, AbstractSpuCollect> spuCollectMap;

    @Autowired
    private EsSpuNewRepository esSpuNewRepository;


    @Override
    protected EsSpuNew packModelId(Object k) {
        EsSpuNew esSpuNew = new EsSpuNew();
        esSpuNew.setSpuId(k.toString());
        return esSpuNew;
    }


    @Override
    protected Class<?> firstLoadCls() {
        return SpuCollect.class;
    }

    @PostConstruct
    public void init() {
        LocalDateTime now = LocalDateTime.now();
        int minSize = 50;
        LocalDateTime lastCollectTime = LocalDateTime.of(2021,12,12,12,12,12,12);
        super.load(spuCollectMap, lastCollectTime, now, minSize);
    }


    @Override
    protected <S> void after(List<S> modelList, LocalDateTime now) {
        if (CollectionUtils.isEmpty(modelList)) {
            return;
        }

        List<EsSpuNew> result = new ArrayList<>(modelList.size());
        for (S s : modelList) {
            EsSpuNew esSpuNew = (EsSpuNew) s;
            esSpuNew.setIndexTime(now);
            result.add(esSpuNew);
        }
        esSpuNewRepository.saveAll(result);
        result.clear(); //帮助gc回收
    }
}
