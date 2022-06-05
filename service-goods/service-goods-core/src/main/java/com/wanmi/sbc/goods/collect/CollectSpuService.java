package com.wanmi.sbc.goods.collect;

import com.wanmi.sbc.goods.api.request.collect.CollectSpuProviderReq;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


    /**
     * 获取商品id列表
     * @param req
     * @return
     */
    public List<GoodsVO> collectSpuIdByTime(CollectSpuProviderReq req) {
        List<Map<String, Object>> goodsMapList = goodsRepository.collectSpuIdByTime(req.getBeginTime(), req.getEndTime(), req.getPageSize());
        List<GoodsVO> result = new ArrayList<>();
        for (Map<String, Object> goodsMapParam : goodsMapList) {
            GoodsVO goodsVO = new GoodsVO();
            goodsVO.setGoodsId(goodsMapParam.get("goods_id").toString());
            goodsVO.setUpdateTime(goodsMapParam.get("update_time") != null ?  ((Timestamp)goodsMapParam.get("update_time")).toLocalDateTime() : null);
            result.add(goodsVO);
        }
        return result;
    }

    /**
     * 获取商品列表 根据商品id
     * @param req
     * @return
     */
    public List<GoodsVO> collectSpuBySpuIds(CollectSpuProviderReq req) {
        List<Goods> goodsList = goodsRepository.findAllByGoodsIdIn(req.getSpuIds());
        List<GoodsVO> result = new ArrayList<>();
        for (Goods goods : goodsList) {
            GoodsVO goodsVO = new GoodsVO();
            BeanUtils.copyProperties(goods, goodsVO);
            result.add(goodsVO);
        }
        return result;
    }
}
