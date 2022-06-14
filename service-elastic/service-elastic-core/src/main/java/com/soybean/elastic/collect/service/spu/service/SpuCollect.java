package com.soybean.elastic.collect.service.spu.service;
import com.soybean.elastic.spu.model.sub.SubAnchorRecomNew;

import com.soybean.elastic.collect.service.spu.AbstractSpuCollect;
import com.soybean.elastic.spu.model.EsSpuNew;
import com.wanmi.sbc.goods.api.enums.AnchorPushEnum;
import com.wanmi.sbc.goods.api.provider.collect.CollectSpuProvider;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuProviderReq;
import com.wanmi.sbc.goods.bean.vo.CollectSpuVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 采集商品信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/1 2:59 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class SpuCollect extends AbstractSpuCollect {

    @Autowired
    private CollectSpuProvider collectSpuProvider;

    /**
     * 获取商品对象信息
     * @param lastCollectTime
     * @param now
     * @return
     */
    private List<CollectSpuVO> getSpuByTime(LocalDateTime lastCollectTime, LocalDateTime now, Integer fromId) {
        CollectSpuProviderReq request = new CollectSpuProviderReq();
        request.setBeginTime(lastCollectTime);
        request.setEndTime(now);
        request.setFromId(fromId);
        request.setPageSize(MAX_PAGE_SIZE);
        return collectSpuProvider.collectSpuIdByTime(request).getContext();
    }

    @Override
    public Set<String> collectId(LocalDateTime lastCollectTime, LocalDateTime now) {
        List<CollectSpuVO> collectSpuVOList = this.getSpuByTime(lastCollectTime, now, 0);
        Set<String> spuIdSet = collectSpuVOList.stream().map(CollectSpuVO::getGoodsId).collect(Collectors.toSet());
        if (collectSpuVOList.size() >= MAX_PAGE_SIZE) {
            Integer tmpId = collectSpuVOList.get(0).getTmpId();
            collectSpuVOList = this.getSpuByTime(lastCollectTime, now, tmpId);
            spuIdSet.addAll(collectSpuVOList.stream().map(CollectSpuVO::getGoodsId).collect(Collectors.toSet()));
        }
        return spuIdSet;
    }



    @Override
    public <T> List<T> collect(List<T> list) {
        List<String> spuIdList = list.stream().map(t -> ((EsSpuNew) t).getSpuId()).collect(Collectors.toList());
        CollectSpuProviderReq req = new CollectSpuProviderReq();
        req.setSpuIds(spuIdList);
        List<CollectSpuVO> context = collectSpuProvider.collectSpuBySpuIds(req).getContext();
        if (CollectionUtils.isEmpty(context)) {
            return list;
        }
        Map<String, CollectSpuVO> spuId2CollectGoodsVoMap =
                context.stream().collect(Collectors.toMap(CollectSpuVO::getGoodsId, Function.identity(), (k1, k2) -> k1));
        for (T t : list) {
            EsSpuNew esSpuNew = (EsSpuNew) t;
            CollectSpuVO collectSpuVO = spuId2CollectGoodsVoMap.get(esSpuNew.getSpuId());
            if (collectSpuVO == null) {
                continue;
            }

            esSpuNew.setSpuId(collectSpuVO.getGoodsId());
            esSpuNew.setSpuName(collectSpuVO.getGoodsName());
            esSpuNew.setSpuSubName(collectSpuVO.getGoodsSubtitle());
//            esSpuNew.setSpuCategory(0);
            esSpuNew.setChannelTypes(collectSpuVO.getGoodsChannelTypes());
            if (!CollectionUtils.isEmpty(collectSpuVO.getAnchorPushs())) {
                List<SubAnchorRecomNew> subAnchorRecomNewList = new ArrayList<>();
                for (String s : collectSpuVO.getAnchorPushs()) {
                    AnchorPushEnum anchorPushEnum = AnchorPushEnum.getByCode(s);
                    if (anchorPushEnum == null) {
                        continue;
                    }
                    SubAnchorRecomNew subAnchorRecomNew = new SubAnchorRecomNew();
                    subAnchorRecomNew.setRecomId(Integer.valueOf(anchorPushEnum.getCode()));
                    subAnchorRecomNew.setRecomName(anchorPushEnum.getMessage());
                    subAnchorRecomNewList.add(subAnchorRecomNew);
                }
                esSpuNew.setAnchorRecoms(subAnchorRecomNewList);
            }

//            esSpuNew.setFavorCommentRate(0.0D);
            esSpuNew.setSalesNum(collectSpuVO.getGoodsSalesNum());
            esSpuNew.setSalesPrice(collectSpuVO.getMiniSalesPrice());
            esSpuNew.setCpsSpecial(collectSpuVO.getCpsSpecial());
            esSpuNew.setAddedFlag(collectSpuVO.getAddedFlag());
            esSpuNew.setAddedTime(collectSpuVO.getAddedTime());
            esSpuNew.setCreateTime(collectSpuVO.getCreateTime());
//            esSpuNew.setIndexTime(LocalDateTime.now());
            esSpuNew.setDelFlag(collectSpuVO.getDelFlag().toValue());
            esSpuNew.setAuditStatus(collectSpuVO.getAuditStatus().toValue());
//            esSpuNew.setBook(new SubBookNew());
//            esSpuNew.setClassifys(Lists.newArrayList());

        }
        return list;
    }
}
