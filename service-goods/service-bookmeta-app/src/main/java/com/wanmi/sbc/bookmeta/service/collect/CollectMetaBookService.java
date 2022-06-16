package com.wanmi.sbc.bookmeta.service.collect;

import com.wanmi.sbc.bookmeta.bo.CollectMetaReq;
import com.wanmi.sbc.bookmeta.entity.MetaBook;
import com.wanmi.sbc.bookmeta.entity.MetaProducer;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 采集图书
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/16 8:59 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CollectMetaBookService extends AbstractCollectBookService{

    /**
     * 采集图书
     * @param collectMetaReq
     */
    public void collectMetaBookByTime(CollectMetaReq collectMetaReq){

        List<MetaBook> metaBooks =
                metaBookMapper.collectMetaBookByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
        
    }
}
