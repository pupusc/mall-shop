package com.soybean.elastic.collect.service.spu.service;

import com.soybean.elastic.collect.service.spu.AbstractSpuCollect;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaReq;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaResp;
import com.wanmi.sbc.bookmeta.provider.collect.CollectMetaBookProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description: 采集图书信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/1 2:59 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class BookContentCollect extends AbstractSpuCollect {

    @Autowired
    protected CollectMetaBookProvider collectMetaBookProvider;

    /**
     * 简介
     * @param beginTime
     * @param endTime
     * @return
     */
    protected CollectMetaResp collectBookByTime(LocalDateTime beginTime, LocalDateTime endTime, Integer fromId) {
        CollectMetaReq req = new CollectMetaReq();
        req.setFromId(fromId);
        req.setPageSize(MAX_PAGE_SIZE);
        req.setBeginTime(beginTime);
        req.setEndTime(endTime);
        return collectMetaBookProvider.collectMetaBookContentByTime(req).getContext();
    }


    @Override
    public Set<String> collectId(LocalDateTime lastCollectTime, LocalDateTime now) {
        CollectMetaResp collectMetaResp = this.collectBookByTime(lastCollectTime, now, 0);
        Set<String> result = new HashSet<>(super.collectSpuPropByIsbn(collectMetaResp));
        while (collectMetaResp.isHasMore()) {
            collectMetaResp = this.collectBookByTime(lastCollectTime, now, collectMetaResp.getLastBizId());
            result.addAll(super.collectSpuPropByIsbn(collectMetaResp));
        }
        return result;
    }



    @Override
    public <T> List<T> collect(List<T> list) {
        //不用处理
        return list;
    }
}
