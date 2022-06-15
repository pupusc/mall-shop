package com.wanmi.sbc.goods.collect;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuProviderReq;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.CollectSpuVO;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/4 1:16 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CollectSpuService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsInfoService goodsInfoService;


    /**
     * 获取商品id列表
     * @param req
     * @return
     */
    public List<CollectSpuVO> collectSpuIdByTime(CollectSpuProviderReq req) {
        List<Map<String, Object>> goodsMapList = goodsRepository.collectSpuIdByTime(req.getBeginTime(), req.getEndTime(), req.getFromId(), req.getPageSize());
        List<CollectSpuVO> result = new ArrayList<>();
        for (Map<String, Object> goodsMapParam : goodsMapList) {
            CollectSpuVO goodsVO = new CollectSpuVO();
            goodsVO.setGoodsId(goodsMapParam.get("goods_id").toString());
            goodsVO.setUpdateTime(goodsMapParam.get("update_time") != null ?  ((Timestamp)goodsMapParam.get("update_time")).toLocalDateTime() : null);
            goodsVO.setTmpId(Integer.valueOf(goodsMapParam.get("tmp_id").toString()));
            result.add(goodsVO);
        }
        return result;
    }

    /**
     * 获取商品列表 根据商品id
     * @param req
     * @return
     */
    public List<CollectSpuVO> collectSpuBySpuIds(CollectSpuProviderReq req) {
        List<Goods> goodsList = goodsRepository.findAllByGoodsIdIn(req.getSpuIds());

        Map<String, GoodsInfo> skuId2GoodsInfoMap = new HashMap<>();
        //获取商品对应的 sku
        if (!CollectionUtils.isEmpty(goodsList)) {
            List<String> spuIdList = goodsList.stream().map(Goods::getGoodsId).collect(Collectors.toList());
            GoodsInfoQueryRequest goodsInfoQueryRequest = new GoodsInfoQueryRequest();
            goodsInfoQueryRequest.setGoodsIds(spuIdList);
            goodsInfoQueryRequest.setAuditStatus(CheckStatus.CHECKED);
            goodsInfoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
            goodsInfoQueryRequest.setAddedFlag(AddedFlag.YES.toValue());
            List<GoodsInfo> goodsInfoList = goodsInfoService.findByParams(goodsInfoQueryRequest);
            for (GoodsInfo goodsInfoParam : goodsInfoList) {
                GoodsInfo goodsInfoTmp = skuId2GoodsInfoMap.get(goodsInfoParam.getGoodsId());
                if (goodsInfoTmp == null) {
                    skuId2GoodsInfoMap.put(goodsInfoParam.getGoodsId(), goodsInfoParam);
                } else {
                    if (goodsInfoTmp.getMarketPrice().compareTo(goodsInfoParam.getMarketPrice()) > 0) {
                        skuId2GoodsInfoMap.put(goodsInfoParam.getGoodsId(), goodsInfoParam);
                    }
                }
            }
        }

        List<CollectSpuVO> result = new ArrayList<>();
        for (Goods goods : goodsList) {
            CollectSpuVO collectSpuVo = new CollectSpuVO();
            BeanUtils.copyProperties(goods, collectSpuVo);
            collectSpuVo.setMinSalePriceImg(skuId2GoodsInfoMap.get(goods.getGoodsId()) == null ? "" : skuId2GoodsInfoMap.get(goods.getGoodsId()).getGoodsInfoImg());
            collectSpuVo.setMiniSalesPrice(skuId2GoodsInfoMap.get(goods.getGoodsId()) == null ? new BigDecimal("9999") : skuId2GoodsInfoMap.get(goods.getGoodsId()).getMarketPrice());
            collectSpuVo.setGoodsChannelTypes(StringUtils.isNotBlank(goods.getGoodsChannelType()) ? Arrays.asList(goods.getGoodsChannelType().split(",")) : new ArrayList<>());
            if (StringUtils.isNotBlank(goods.getAnchorPushs())) {
                collectSpuVo.setAnchorPushs(Arrays.asList(goods.getAnchorPushs().split(" ")));
            } else {
                collectSpuVo.setAnchorPushs(new ArrayList<>());
            }
            result.add(collectSpuVo);
        }
        return result;
    }
}
