package com.wanmi.sbc.bookmeta.service.collect;

import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaReq;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaResp;
import com.wanmi.sbc.bookmeta.entity.MetaProducer;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: 采集出品方
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/16 8:59 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CollectMetaBookProducerService extends AbstractCollectBookService{

    /**
     * 采集出品方
     * @param collectMetaReq
     */
    public CollectMetaResp collectMetaBookProducerByTime(CollectMetaReq collectMetaReq){
        CollectMetaResp collectMetaResp = new CollectMetaResp();
        List<MetaProducer> metaProducers = metaProducerMapper.collectMetaProducerByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
        if (CollectionUtils.isEmpty(metaProducers)) {
            return collectMetaResp;
        }
        collectMetaResp.setLastBizId(metaProducers.get(metaProducers.size() -1).getId());
        collectMetaResp.setHasMore(metaProducers.size() >= collectMetaReq.getPageSize());
        List<Integer> producerIds = metaProducers.stream().map(MetaProducer::getId).collect(Collectors.toList());
        collectMetaResp.setMetaBookResps(super.collectBookByBookProducerIds(producerIds));
        //根据出品方获取商品信息
        return collectMetaResp;
    }
}
