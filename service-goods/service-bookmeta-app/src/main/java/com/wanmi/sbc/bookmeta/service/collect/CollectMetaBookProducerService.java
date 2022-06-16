package com.wanmi.sbc.bookmeta.service.collect;

import com.wanmi.sbc.bookmeta.bo.CollectMetaReq;
import com.wanmi.sbc.bookmeta.entity.MetaProducer;
import com.wanmi.sbc.bookmeta.entity.MetaPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void collectMetaBookProducerByTime(CollectMetaReq collectMetaReq){
        List<MetaProducer> metaProducers = metaProducerMapper.collectMetaProducerByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
        //根据出品方获取商品信息
    }
}
