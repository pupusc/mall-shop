package com.wanmi.sbc.goods.provider.impl.ares;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.MarketingLabelVO;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: wanggang
 * @createDate: 2018/11/7 19:05
 * @version: 1.0
 */
public class GoodsAresConvert {

    private GoodsAresConvert(){

    }

    /**
     * List<GoodsInfo> 对象 转换成 List<GoodsInfoVO> 对象
     * @param goodsInfoList
     * @return
     */
    public static List<GoodsInfoVO> toVO(List<GoodsInfo> goodsInfoList){
        return goodsInfoList.stream().map(goodsInfo -> {
            GoodsInfoVO goodsInfoVO = new GoodsInfoVO();
            KsBeanUtil.copyPropertiesThird(goodsInfo,goodsInfoVO);
            goodsInfoVO.setMarketingLabels(goodsInfo.getMarketingLabels().stream().map(marketingLabel -> {
                MarketingLabelVO marketingLabelVO = new MarketingLabelVO();
                KsBeanUtil.copyPropertiesThird(marketingLabel,marketingLabelVO);
                return marketingLabelVO;
            }).collect(Collectors.toList()));
            return goodsInfoVO;
        }).collect(Collectors.toList());
    }
}
