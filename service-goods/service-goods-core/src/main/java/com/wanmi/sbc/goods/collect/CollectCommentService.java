package com.wanmi.sbc.goods.collect;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.request.collect.CollectCommentProviderReq;
import com.wanmi.sbc.goods.api.response.collect.CollectCommentRelSpuDetailResp;
import com.wanmi.sbc.goods.api.response.collect.CollectCommentRelSpuResp;
import com.wanmi.sbc.goods.goodsevaluate.model.root.GoodsEvaluate;
import com.wanmi.sbc.goods.goodsevaluate.repository.GoodsEvaluateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description: 采集店铺分类
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/14 2:05 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class CollectCommentService {

    @Autowired
    private GoodsEvaluateRepository classifyRepository;


    /**
     * 按照时间获取评论对应的商品信息
     * @param req
     * @return
     */
    public List<CollectCommentRelSpuResp> collectCommentSpuIdByTime(CollectCommentProviderReq req) {
        List<CollectCommentRelSpuResp> commentRelSpuResps = new ArrayList<>();
        List<GoodsEvaluate> goodsEvaluateList =
                classifyRepository.collectCommentSpuIdByTime(req.getBeginTime(), req.getEndTime(), req.getIncrId(), req.getPageSize());
        for (GoodsEvaluate goodsEvaluate : goodsEvaluateList) {
            CollectCommentRelSpuResp collectClassifyRelSpuResp = new CollectCommentRelSpuResp();
            collectClassifyRelSpuResp.setCommentId(goodsEvaluate.getEvaluateId());
            collectClassifyRelSpuResp.setSpuId(goodsEvaluate.getGoodsId());
            collectClassifyRelSpuResp.setIncrId(goodsEvaluate.getIncrId());
            commentRelSpuResps.add(collectClassifyRelSpuResp);
        }
        return commentRelSpuResps;
    }

    /**
     * 根据商品id采集评论信息
     * @param req
     * @return
     */
    public List<CollectCommentRelSpuDetailResp> collectCommentBySpuIds(CollectCommentProviderReq req) {
        List<Map> mapList = classifyRepository.collectCommentSumBySpuIds(req.getSpuIds());

        List<CollectCommentRelSpuDetailResp> commentList = KsBeanUtil.convert(mapList, CollectCommentRelSpuDetailResp.class);
        if (commentList != null && commentList.size() != 0) {
            List<Map> mapGoodsCommentList = classifyRepository.collectCommentGoodSumBySpuIds(req.getSpuIds());
            List<CollectCommentRelSpuDetailResp> goodsCommentList = KsBeanUtil.convert(mapGoodsCommentList, CollectCommentRelSpuDetailResp.class);
            Map<String, BigDecimal> goodsCommentMap = goodsCommentList.stream().collect(Collectors.toMap(CollectCommentRelSpuDetailResp::getGoodsId, CollectCommentRelSpuDetailResp::getGoodEvaluateSum));
            for (CollectCommentRelSpuDetailResp comment : commentList) {
                if (goodsCommentMap.get(comment.getGoodsId()) != null) {
                    comment.setGoodEvaluateRatio(goodsCommentMap.get(comment.getGoodsId()).divide(comment.getEvaluateSum(), 2, RoundingMode.HALF_UP).toString());
                } else {
                    comment.setGoodEvaluateRatio("0");
                }
            }
        }
        return commentList;
    }

}
