package com.wanmi.sbc.bookmeta.service.collect;

import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaReq;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaResp;
import com.wanmi.sbc.bookmeta.entity.MetaPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Description: 采集出版社
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/16 8:59 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CollectMetaBookPublisherService extends AbstractCollectBookService{

    /**
     * 采集出版社
     * @param collectMetaReq
     */
    public CollectMetaResp collectMetaBookPublisherByTime(CollectMetaReq collectMetaReq){
        CollectMetaResp collectMetaResp = new CollectMetaResp();
        List<MetaPublisher> metaPublishers = metaPublisherMapper.collectMetaPublisherByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
        if (CollectionUtils.isEmpty(metaPublishers)) {
            return collectMetaResp;
        }
        collectMetaResp.setLastBizId(metaPublishers.get(metaPublishers.size() -1).getId());
        //根据出版社获取 商品信息
        return collectMetaResp;
    }
}
