package com.wanmi.sbc.bookmeta.service.collect;

import com.wanmi.sbc.bookmeta.bo.CollectMetaReq;
import com.wanmi.sbc.bookmeta.entity.MetaBookLabel;
import org.springframework.stereotype.Service;

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
    public void collectMetaBookLabelByTime(CollectMetaReq collectMetaReq){

        List<MetaBookLabel> metaBookLabels = metaBookLabelMapper.collectMetaBookLabelByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
        //获取图书信息
    }
}
