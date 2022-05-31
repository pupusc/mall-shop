package com.wanmi.sbc.goods.info.service;

import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.reponse.GoodsInfoResponse;
import com.wanmi.sbc.goods.info.request.GoodsInfoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/22 1:27 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class VideoChannelSetFilterService {

    @Autowired
    private GoodsInfoService goodsInfoService;


    /**
     * 获取商品对应的视频号渠道信息
     * @param skuIdList
     * @return
     */
    public Map<String, Boolean> filterGoodsIdHasVideoChannelMap(List<String> skuIdList) {
        Map<String, Boolean> result = new HashMap<>();
        GoodsInfoRequest goodsInfoRequest = new GoodsInfoRequest();
        goodsInfoRequest.setGoodsInfoIds(skuIdList);
        List<GoodsInfo> goodsInfoList = goodsInfoService.findSkuByIds(goodsInfoRequest).getGoodsInfos();

        for (GoodsInfo goodsInfoParam : goodsInfoList) {
            String goodsChannelType = goodsInfoParam.getGoodsChannelType();
            if (StringUtils.isEmpty(goodsChannelType)) {
                log.error("VideoChannelSetFilterService filterVideoChannelSet goodsId:{} channelType is empty", goodsInfoParam.getGoodsId());
                continue;
            }
            log.error("VideoChannelSetFilterService filterVideoChannelSet goodsId:{} channelType {}", goodsInfoParam.getGoodsId(), goodsInfoParam.getGoodsChannelType());
            Boolean goodsIdResult = result.get(goodsInfoParam.getGoodsId());
            if (goodsIdResult == null || !goodsIdResult) {
                String[] goodsChannelTypeAttr = goodsChannelType.split(",");
                for (String goodsChannelTypeParam : goodsChannelTypeAttr) {
                    if (goodsChannelTypeParam.equals(TerminalSource.MALL_NORMAL.getCode()+"")) {
                        result.put(goodsInfoParam.getGoodsId(), true);
                    }
                }
            }
        }
        return result;
    }
}
