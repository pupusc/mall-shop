package com.wanmi.sbc.goods.collect;

import com.wanmi.sbc.goods.api.request.collect.CollectSpuImageProviderReq;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuPropProviderReq;
import com.wanmi.sbc.goods.api.response.collect.CollectSpuImageResp;
import com.wanmi.sbc.goods.api.response.collect.CollectSpuPropResp;
import com.wanmi.sbc.goods.images.GoodsImage;
import com.wanmi.sbc.goods.images.GoodsImageRepository;
import com.wanmi.sbc.goods.info.model.root.GoodsPropDetailRel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 采集商品属性信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/14 6:04 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CollectSpuImageService {


    @Autowired
    private GoodsImageRepository goodsImageRepository;

    /**
     * 获取商品id列表
     * @param req
     * @return
     */
    public List<CollectSpuImageResp> collectSpuIdImageByTime(CollectSpuImageProviderReq req) {
        List<CollectSpuImageResp> result = new ArrayList<>();
        List<GoodsImage> goodsImageList =
                goodsImageRepository.collectSpuIdImageByTime(req.getBeginTime(), req.getEndTime(), req.getFromId(), req.getPageSize());
        for (GoodsImage goodsImage : goodsImageList) {
            CollectSpuImageResp collectSpuImageResp = new CollectSpuImageResp();
            collectSpuImageResp.setTmpId(goodsImage.getImageId().intValue());
            collectSpuImageResp.setSpuId(goodsImage.getGoodsId());
            result.add(collectSpuImageResp);
        }
        return result;
    }

}
