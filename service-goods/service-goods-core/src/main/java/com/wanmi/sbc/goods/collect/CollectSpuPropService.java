package com.wanmi.sbc.goods.collect;
import java.math.BigDecimal;

import com.wanmi.sbc.goods.api.request.collect.CollectSpuPropProviderReq;
import com.wanmi.sbc.goods.api.response.collect.CollectSpuPropResp;
import com.wanmi.sbc.goods.bean.vo.CollectSpuVO;
import com.wanmi.sbc.goods.info.model.root.GoodsPropDetailRel;
import com.wanmi.sbc.goods.info.repository.GoodsPropDetailRelRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Description: 采集商品属性信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/14 6:04 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CollectSpuPropService {

    @Autowired
    private GoodsPropDetailRelRepository goodsPropDetailRelRepository;

    //表示的是 3 评分 4 定价 5 ISBN
    private final List<Integer> propIdList = new ArrayList<>(Arrays.asList(3,4,5));

    /**
     * 获取商品id列表
     * @param req
     * @return
     */
    public List<CollectSpuPropResp> collectSpuIdPropByTime(CollectSpuPropProviderReq req) {
        List<GoodsPropDetailRel> goodsPropDetailRelList =
                goodsPropDetailRelRepository.collectSpuIdPropByTime(req.getBeginTime(), req.getEndTime(), req.getFromId(), req.getPageSize());
        List<CollectSpuPropResp> spuPropRespList = new ArrayList<>();
        for (GoodsPropDetailRel goodsPropDetailRel : goodsPropDetailRelList) {
            if (propIdList.contains(goodsPropDetailRel.getPropId().intValue())) {
                CollectSpuPropResp collectSpuPropResp = new CollectSpuPropResp();
                collectSpuPropResp.setTmpId(goodsPropDetailRel.getRelId().intValue());
                collectSpuPropResp.setSpuId(goodsPropDetailRel.getGoodsId());
                spuPropRespList.add(collectSpuPropResp);
            }
        }
        return spuPropRespList;
    }


    /**
     * 根据商品id获取商品属性信息
     * @param req
     * @return
     */
    public List<CollectSpuPropResp> collectSpuPropBySpuIds(CollectSpuPropProviderReq req){
        List<GoodsPropDetailRel> goodsPropDetailRels = goodsPropDetailRelRepository.collectSpuIdPropBySpuIds(req.getSpuIds());
        return new ArrayList<>(this.changeSpuPropMap(goodsPropDetailRels).values());
    }

    /**
     * 根据isbn获取商品id信息
     * @param req
     * @return
     */
    public List<CollectSpuPropResp> collectSpuPropByISBN(CollectSpuPropProviderReq req) {
        if (CollectionUtils.isEmpty(req.getIsbn())) {
            return new ArrayList<>();
        }
        List<GoodsPropDetailRel> goodsPropDetailRels = goodsPropDetailRelRepository.collectSpuIdPropByIsbns(req.getIsbn(), Collections.singletonList(5));
        return new ArrayList<>(this.changeSpuPropMap(goodsPropDetailRels).values());
    }

    private Map<String, CollectSpuPropResp> changeSpuPropMap(List<GoodsPropDetailRel> goodsPropDetailRels) {
        Map<String, CollectSpuPropResp> spuId2CollectSpuPropMap = new HashMap<>();
        for (GoodsPropDetailRel goodsPropDetailRelParam : goodsPropDetailRels) {
            CollectSpuPropResp collectSpuPropResp = spuId2CollectSpuPropMap.getOrDefault(goodsPropDetailRelParam.getGoodsId(), new CollectSpuPropResp());
            if (Objects.equals(3, goodsPropDetailRelParam.getPropId().intValue())) {
                collectSpuPropResp.setScore(StringUtils.isNotBlank(goodsPropDetailRelParam.getPropValue()) ? Double.parseDouble(goodsPropDetailRelParam.getPropValue()) : 0.0);
            }
            if (Objects.equals(4, goodsPropDetailRelParam.getPropId().intValue())) {
                collectSpuPropResp.setFixPrice(StringUtils.isNotBlank(goodsPropDetailRelParam.getPropValue()) ? new BigDecimal(goodsPropDetailRelParam.getPropValue()) : new BigDecimal("9999"));
            }
            if (Objects.equals(5, goodsPropDetailRelParam.getPropId().intValue())) {
                collectSpuPropResp.setIsbn(StringUtils.isNotBlank(goodsPropDetailRelParam.getPropValue()) ? goodsPropDetailRelParam.getPropValue() : "");
            }
            if (propIdList.contains(goodsPropDetailRelParam.getPropId().intValue())) {
                collectSpuPropResp.setSpuId(goodsPropDetailRelParam.getGoodsId());
            }
            spuId2CollectSpuPropMap.put(goodsPropDetailRelParam.getGoodsId(), collectSpuPropResp);
        }
        return spuId2CollectSpuPropMap;
    }
}
