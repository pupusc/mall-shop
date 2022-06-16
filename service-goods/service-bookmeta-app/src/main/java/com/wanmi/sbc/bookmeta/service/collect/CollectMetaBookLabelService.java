package com.wanmi.sbc.bookmeta.service.collect;

import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaReq;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaResp;
import com.wanmi.sbc.bookmeta.entity.MetaBookLabel;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * Description: 标签信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/16 8:59 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CollectMetaBookLabelService extends AbstractCollectBookService{

    /**
     * 采集 标签信息
     * @param collectMetaReq
     */
    public CollectMetaResp collectMetaBookLabelByTime(CollectMetaReq collectMetaReq){
        CollectMetaResp collectMetaResp = new CollectMetaResp();
        List<MetaBookLabel> metaBookLabels = metaBookLabelMapper.collectMetaBookLabelByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
        if (CollectionUtils.isEmpty(metaBookLabels)) {
            return collectMetaResp;
        }
        collectMetaResp.setLastBizId(metaBookLabels.get(metaBookLabels.size() -1).getId());
        //获取图书信息
        return collectMetaResp;
    }
}
