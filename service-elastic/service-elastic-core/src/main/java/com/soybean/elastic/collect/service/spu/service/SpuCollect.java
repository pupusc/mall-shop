package com.soybean.elastic.collect.service.spu.service;
import com.soybean.elastic.spu.model.sub.SubAnchorRecomNew;

import com.soybean.elastic.collect.service.spu.AbstractSpuCollect;
import com.soybean.elastic.spu.model.EsSpuNew;
import com.wanmi.sbc.goods.api.enums.AnchorPushEnum;
import com.wanmi.sbc.goods.api.provider.collect.CollectSpuProvider;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuProviderReq;
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
    private List<GoodsVO> getSpuByTime(LocalDateTime lastCollectTime, LocalDateTime now) {
        CollectSpuProviderReq request = new CollectSpuProviderReq();
        request.setBeginTime(lastCollectTime);
        request.setEndTime(now);
        request.setPageSize(MAX_PAGE_SIZE);
        return collectSpuProvider.collectSpuIdByTime(request).getContext();
    }

    @Override
    public Set<String> collectId(LocalDateTime lastCollectTime, LocalDateTime now) {
        List<GoodsVO> goodsVOList = this.getSpuByTime(lastCollectTime, now);
        Set<String> spuIdSet = goodsVOList.stream().map(GoodsVO::getGoodsId).collect(Collectors.toSet());
        if (goodsVOList.size() >= MAX_PAGE_SIZE) {
            LocalDateTime updateTime = goodsVOList.get(0).getUpdateTime();
            goodsVOList = this.getSpuByTime(updateTime, now);
            spuIdSet.addAll(goodsVOList.stream().map(GoodsVO::getGoodsId).collect(Collectors.toSet()));
        }
        return spuIdSet;
    }



    @Override
    public <T> List<T> collect(List<T> list) {
        List<String> spuIdList = list.stream().map(t -> ((EsSpuNew) t).getSpuId()).collect(Collectors.toList());
        CollectSpuProviderReq req = new CollectSpuProviderReq();
        req.setSpuIds(spuIdList);
        List<GoodsVO> context = collectSpuProvider.collectSpuBySpuIds(req).getContext();
        if (CollectionUtils.isEmpty(context)) {
            return list;
        }
        Map<String, GoodsVO> spuId2GoodsVoMap =
                context.stream().collect(Collectors.toMap(GoodsVO::getGoodsId, Function.identity(), (k1, k2) -> k1));
        for (T t : list) {
            EsSpuNew esSpuNew = (EsSpuNew) t;
            GoodsVO goodsVO = spuId2GoodsVoMap.get(esSpuNew.getSpuId());
            if (goodsVO == null) {
                continue;
            }

            esSpuNew.setSpuId(goodsVO.getGoodsId());
            esSpuNew.setSpuName(goodsVO.getGoodsName());
            esSpuNew.setSpuSubName(goodsVO.getGoodsSubtitle());
//            esSpuNew.setSpuCategory(0);
            esSpuNew.setSpuChannels(StringUtils.isNotBlank(goodsVO.getGoodsChannelType()) ? Arrays.asList(goodsVO.getGoodsChannelType().split(",")) : new ArrayList<>());
            if (StringUtils.isNotBlank(goodsVO.getAnchorPushs())) {
                List<SubAnchorRecomNew> subAnchorRecomNewList = new ArrayList<>();
                for (String s : goodsVO.getAnchorPushs().split(" ")) {
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
            esSpuNew.setSalesNum(goodsVO.getGoodsSalesNum());
//            esSpuNew.setSalesPrice(goodsVO);
            esSpuNew.setCpsSpecial(goodsVO.getCpsSpecial());
            esSpuNew.setAddedFlag(goodsVO.getAddedFlag());
            esSpuNew.setAddedTime(goodsVO.getAddedTime());
            esSpuNew.setCreateTime(goodsVO.getCreateTime());
//            esSpuNew.setIndexTime(LocalDateTime.now());
            esSpuNew.setDelFlag(goodsVO.getDelFlag().toValue());
            esSpuNew.setAuditStatus(goodsVO.getAuditStatus().toValue());
//            esSpuNew.setBook(new SubBookNew());
//            esSpuNew.setClassifys(Lists.newArrayList());

        }
        return list;
    }
}
