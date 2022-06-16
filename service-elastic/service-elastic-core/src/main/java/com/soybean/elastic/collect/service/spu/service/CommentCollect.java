package com.soybean.elastic.collect.service.spu.service;

import com.soybean.elastic.collect.service.spu.AbstractSpuCollect;
import com.soybean.elastic.spu.model.EsSpuNew;
import com.soybean.elastic.spu.model.sub.SubCommentNew;
import com.wanmi.sbc.goods.api.provider.collect.CollectCommentProvider;
import com.wanmi.sbc.goods.api.request.collect.CollectCommentProviderReq;
import com.wanmi.sbc.goods.api.response.collect.CollectCommentRelSpuDetailResp;
import com.wanmi.sbc.goods.api.response.collect.CollectCommentRelSpuResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 采集评论信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/13 3:31 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public class CommentCollect extends AbstractSpuCollect {

    @Autowired
    private CollectCommentProvider collectCommentProvider;

    @Override
    public Set<String> collectId(LocalDateTime lastCollectTime, LocalDateTime now) {
        Set<String> spuIdSet = new HashSet<>();
        CollectCommentProviderReq req = new CollectCommentProviderReq();
        req.setPageSize(MAX_PAGE_SIZE);
        req.setBeginTime(lastCollectTime);
        req.setEndTime(now);
        req.setIncrId(0L);
        List<CollectCommentRelSpuResp> commentRelSpuResps = collectCommentProvider.collectCommentSpuIdByTime(req).getContext();

        for (CollectCommentRelSpuResp collectClassifyRelSpuResp : commentRelSpuResps) {
            spuIdSet.add(collectClassifyRelSpuResp.getSpuId());
        }

        while (commentRelSpuResps.size() >= MAX_PAGE_SIZE) {
            Long incrId = commentRelSpuResps.get(commentRelSpuResps.size() - 1).getIncrId();
            req.setIncrId(incrId);
            commentRelSpuResps = collectCommentProvider.collectCommentSpuIdByTime(req).getContext();
            for (CollectCommentRelSpuResp collectClassifyRelSpuResp : commentRelSpuResps) {
                spuIdSet.add(collectClassifyRelSpuResp.getSpuId());
            }
        }
        return spuIdSet;
    }

    @Override
    public <F> List<F> collect(List<F> list) {
        List<String> spuIdList = list.stream().map(t -> ((EsSpuNew) t).getSpuId()).collect(Collectors.toList());
        CollectCommentProviderReq req = new CollectCommentProviderReq();
        req.setSpuIds(spuIdList);
        List<CollectCommentRelSpuDetailResp> context = collectCommentProvider.collectCommentBySpuIds(req).getContext();
        if (CollectionUtils.isEmpty(context)) {
            return list;
        }
        Map<String, CollectCommentRelSpuDetailResp> supMap =
                context.stream().collect(Collectors.toMap(CollectCommentRelSpuDetailResp::getGoodsId, Function.identity(), (k1, k2) -> k1));
        for (F f : list) {
            EsSpuNew esSpuNew = (EsSpuNew) f;
            CollectCommentRelSpuDetailResp collectClassifyRelSpuDetailResp = supMap.get(esSpuNew.getSpuId());
            if (collectClassifyRelSpuDetailResp == null) {
                continue;
            }
            SubCommentNew subCommentNew = new SubCommentNew();
            subCommentNew.setEvaluateSum(collectClassifyRelSpuDetailResp.getEvaluateSum().intValue());
            subCommentNew.setGoodEvaluateSum(collectClassifyRelSpuDetailResp.getGoodEvaluateSum().intValue());
            subCommentNew.setGoodEvaluateRatio(collectClassifyRelSpuDetailResp.getGoodEvaluateRatio());
            esSpuNew.setComment(subCommentNew);
        }
        return list;
    }
}
