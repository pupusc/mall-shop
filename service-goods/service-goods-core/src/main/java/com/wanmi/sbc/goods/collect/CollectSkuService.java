package com.wanmi.sbc.goods.collect;

import com.wanmi.sbc.goods.api.request.collect.CollectSpuProviderReq;
import com.wanmi.sbc.goods.bean.vo.CollectSkuVO;
import com.wanmi.sbc.goods.bean.vo.CollectSpuVO;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/19 1:37 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CollectSkuService {

    @Autowired
    private GoodsInfoRepository goodsInfoRepository;

    /**
     * 获取商品id列表
     * @param req
     * @return
     */
    public List<CollectSkuVO> collectSkuIdByTime(CollectSpuProviderReq req) {

        List<Map<String, Object>> goodsInfoMapList = goodsInfoRepository.collectSkuIdByTime(req.getBeginTime(), req.getEndTime(), req.getFromId(), req.getPageSize());
        List<CollectSkuVO> result = new ArrayList<>();
        for (Map<String, Object> goodsInfoMapParam : goodsInfoMapList) {
            CollectSkuVO skuVO = new CollectSkuVO();
            skuVO.setGoodsId(goodsInfoMapParam.get("goods_id").toString());
            skuVO.setGoodsInfoId(goodsInfoMapParam.get("goods_info_id").toString());
            skuVO.setUpdateTime(goodsInfoMapParam.get("update_time") != null ?  ((Timestamp)goodsInfoMapParam.get("update_time")).toLocalDateTime() : null);
            skuVO.setTmpId(Integer.valueOf(goodsInfoMapParam.get("tmp_id").toString()));
            result.add(skuVO);
        }
        return result;
    }


    public List<CollectSkuVO> collectSkuBySpuIds(CollectSpuProviderReq req) {
        List<CollectSkuVO> result = new ArrayList<>();
        List<Map<String, Object>> goodsInfoMapList = goodsInfoRepository.collectSkuBySpuIds(req.getSpuIds());
        for (Map<String, Object> goodsInfoMapParam : goodsInfoMapList) {
            CollectSkuVO skuVO = new CollectSkuVO();
            skuVO.setGoodsId(goodsInfoMapParam.get("goods_id").toString());
            skuVO.setGoodsInfoId(goodsInfoMapParam.get("goods_info_id").toString());
            skuVO.setUpdateTime(goodsInfoMapParam.get("update_time") != null ?  ((Timestamp)goodsInfoMapParam.get("update_time")).toLocalDateTime() : null);
            skuVO.setMarketPrice(new BigDecimal(goodsInfoMapParam.get("market_price") == null ? "0" : goodsInfoMapParam.get("market_price").toString()));
            skuVO.setCostPrice(new BigDecimal(goodsInfoMapParam.get("cost_price") == null ? "0" : goodsInfoMapParam.get("cost_price").toString()));
            skuVO.setTmpId(Integer.valueOf(goodsInfoMapParam.get("tmp_id").toString()));
            result.add(skuVO);
        }
        return result;
    }
}
