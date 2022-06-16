package com.wanmi.sbc.bookmeta.service.collect;

import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaReq;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaResp;
import com.wanmi.sbc.bookmeta.entity.MetaBookClump;
import com.wanmi.sbc.bookmeta.entity.MetaPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/16 8:59 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CollectMetaBookClumpService extends AbstractCollectBookService{

    /**
     * 采集
     * @param collectMetaReq
     */
    public CollectMetaResp collectMetaBookClumpByTime(CollectMetaReq collectMetaReq){

        CollectMetaResp collectMetaResp = new CollectMetaResp();
        //获取从书
        List<MetaBookClump> metaBookClumps = metaBookClumpMapper.collectMetaBookClumpByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
        if (CollectionUtils.isEmpty(metaBookClumps)) {
            return collectMetaResp;
        }
        collectMetaResp.setLastBizId(metaBookClumps.get(metaBookClumps.size() -1).getId());
        collectMetaResp.setHasMore(metaBookClumps.size() >= collectMetaReq.getPageSize());

        //获取丛书出版社
        List<Integer> publisherIds = metaBookClumps.stream().map(MetaBookClump::getPublisherId).collect(Collectors.toList());
//        List<MetaPublisher> metaPublishers = metaPublisherMapper.collectMetaPublisherByIds(publisherIds);
//        List<Integer> publisherIds = metaPublishers.stream().map(MetaPublisher::getId).collect(Collectors.toList());
        collectMetaResp.setMetaBookResps(super.collectBookByPublisherIds(publisherIds));
        //根据出版社获取 商品信息
        return collectMetaResp;
    }
}
