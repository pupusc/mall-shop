package com.soybean.elastic.collect.service.spu.service;

import com.soybean.elastic.collect.service.booklistmodel.AbstractBookListModelCollect;
import com.soybean.elastic.collect.service.spu.AbstractSpuCollect;
import com.wanmi.sbc.goods.api.provider.collect.CollectSpuProvider;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuImageProviderReq;
import com.wanmi.sbc.goods.api.response.collect.CollectSpuImageResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/18 2:57 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class SpuImageCollect extends AbstractSpuCollect {

    @Autowired
    private CollectSpuProvider collectSpuProvider;

    /**
     * 采集商品图片信息
     * @param beginTime
     * @param endTime
     * @param fromId
     * @return
     */
    private List<CollectSpuImageResp> collectSpuImageRespsByTime (LocalDateTime beginTime, LocalDateTime endTime, Integer fromId) {
        CollectSpuImageProviderReq collectSpuImageProviderReq = new CollectSpuImageProviderReq();
        collectSpuImageProviderReq.setPageSize(MAX_PAGE_SIZE);
        collectSpuImageProviderReq.setFromId(fromId);
        collectSpuImageProviderReq.setBeginTime(beginTime);
        collectSpuImageProviderReq.setEndTime(endTime);
        return collectSpuProvider.collectSpuIdImageByTime(collectSpuImageProviderReq).getContext();
    }
    
    @Override
    public Set<String> collectId(LocalDateTime lastCollectTime, LocalDateTime now) {
        List<CollectSpuImageResp> collectSpuImageList = this.collectSpuImageRespsByTime(lastCollectTime, now, 0);
        Set<String> spuIdSet = new HashSet<>(collectSpuImageList.stream().map(CollectSpuImageResp::getSpuId).collect(Collectors.toSet()));
        while (collectSpuImageList.size() >= MAX_PAGE_SIZE) {
            Integer tmpId = collectSpuImageList.get(collectSpuImageList.size() - 1).getTmpId();
            collectSpuImageList = this.collectSpuImageRespsByTime(lastCollectTime, now, tmpId);
            spuIdSet.addAll(collectSpuImageList.stream().map(CollectSpuImageResp::getSpuId).collect(Collectors.toSet()));
        }
        return spuIdSet;
    }

    @Override
    public <F> List<F> collect(List<F> list) {
        //此处不用处理
        return list;
    }
}
