package com.wanmi.sbc.bookmeta.service.collect;

import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaReq;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaResp;
import com.wanmi.sbc.bookmeta.entity.MetaBookGroup;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Description: 采集 书组名称
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/16 8:59 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CollectMetaBookGroupService extends AbstractCollectBookService{

    /**
     * 采集 书组名称
     * @param collectMetaReq
     */
    public CollectMetaResp collectMetaBookGroupByTime(CollectMetaReq collectMetaReq){
        CollectMetaResp collectMetaResp = new CollectMetaResp();
        List<MetaBookGroup> metaBookGroup = metaBookGroupMapper.collectMetaBookGroupByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
        if (CollectionUtils.isEmpty(metaBookGroup)) {
            return collectMetaResp;
        }
        collectMetaResp.setLastBizId(metaBookGroup.get(metaBookGroup.size() -1).getId());
        //根据出品方获取商品信息
        return collectMetaResp;
    }
}
