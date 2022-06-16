package com.wanmi.sbc.bookmeta.service.collect;

import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaReq;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaResp;
import com.wanmi.sbc.bookmeta.entity.MetaBookFigure;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Description: 采集作者信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/16 8:34 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CollectMetaFigureRelService extends AbstractCollectBookService {

    /**
     * 采集 作者信息
     * @param collectMetaReq
     */
    public CollectMetaResp collectMetaFigureRelByTime(CollectMetaReq collectMetaReq){
        CollectMetaResp collectMetaResp = new CollectMetaResp();
        List<MetaBookFigure> metaBookFigures =
                metaBookFigureMapper.collectMetaBookFigureByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
        if (CollectionUtils.isEmpty(metaBookFigures)) {
            return collectMetaResp;
        }
        collectMetaResp.setLastBizId(metaBookFigures.get(metaBookFigures.size() -1).getId());
        //获取图书信息
        return collectMetaResp;
    }


}
