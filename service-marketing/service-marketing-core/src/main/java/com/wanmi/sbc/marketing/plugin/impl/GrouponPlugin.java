package com.wanmi.sbc.marketing.plugin.impl;

import com.wanmi.sbc.goods.api.response.info.GoodsInfoDetailByGoodsInfoResponse;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GrouponLabelVO;
import com.wanmi.sbc.marketing.grouponactivity.service.GrouponActivityService;
import com.wanmi.sbc.marketing.plugin.IGoodsDetailPlugin;
import com.wanmi.sbc.marketing.plugin.IGoodsListPlugin;
import com.wanmi.sbc.marketing.request.MarketingPluginRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 营销拼团活动插件
 * Created by lq .
 */
@Repository("grouponPlugin")
public class GrouponPlugin implements IGoodsListPlugin, IGoodsDetailPlugin {

    @Autowired
    private GrouponActivityService grouponActivityService;

    /**
     * 商品列表处理
     *
     * @param goodsInfos 商品数据
     * @param request    参数
     */
    @Override
    public void goodsListFilter(List<GoodsInfoVO> goodsInfos, MarketingPluginRequest request) {
        if (request.getCommitFlag()){
            return;
        }
        //查询进行中的拼团活动-排除分销商品
        List<String> skuIdList = goodsInfos.stream().map
                (GoodsInfoVO::getGoodsInfoId).distinct().collect(Collectors.toList());
        Map<String, GrouponGoodsInfoVO> skus = grouponActivityService.listActivityingWithGoodsInfo(skuIdList);

        goodsInfos.forEach(goodsInfoVO -> {
            if (Objects.nonNull(skus) && Objects.nonNull(skus.get(goodsInfoVO.getGoodsInfoId()))) {
                GrouponGoodsInfoVO vo=skus.get(goodsInfoVO.getGoodsInfoId());
                goodsInfoVO.setGrouponLabel(GrouponLabelVO.builder().marketingDesc("拼团").grouponActivityId(vo.getGrouponActivityId()).build
                        ());
            }
        });
    }

    /**
     * 商品详情处理-排除分销商品
     *
     * @param detailResponse 商品详情数据
     * @param request        参数
     */
    @Override
    public void goodsDetailFilter(GoodsInfoDetailByGoodsInfoResponse detailResponse, MarketingPluginRequest request) {
        GoodsInfoVO goodsInfo = detailResponse.getGoodsInfo();
        //查询进行中的拼团活动(spu)
        List<String> goodsIds = Collections.singletonList(goodsInfo).stream().filter(info -> !Objects.equals(info
                .getDistributionGoodsAudit(), DistributionGoodsAudit.CHECKED)).map
                (GoodsInfoVO::getGoodsId).distinct().collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(goodsIds)){
            List<String> grouponActivitys = grouponActivityService.listActivityingSpuIds( goodsIds);
            if (CollectionUtils.isNotEmpty(grouponActivitys)) {
                detailResponse.setGrouponFlag(Boolean.TRUE);
            }
        }
    }
}