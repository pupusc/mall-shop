package com.wanmi.sbc.marketing.points.service;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.bean.dto.GoodsSuitsDTO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsSyncVO;
import com.wanmi.sbc.marketing.api.request.marketingsuits.MarketingSuitsSaveRequest;
import com.wanmi.sbc.marketing.api.request.points.PointsExchangeAcitvityAddRequest;
import com.wanmi.sbc.marketing.api.request.points.PointsExchangePageRequest;
import com.wanmi.sbc.marketing.bean.enums.MarketingScopeType;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.bean.vo.PointsExchangeActivityGoodsVO;
import com.wanmi.sbc.marketing.bean.vo.PointsExchangeActivityVO;
import com.wanmi.sbc.marketing.common.model.root.Marketing;
import com.wanmi.sbc.marketing.common.request.MarketingSaveRequest;
import com.wanmi.sbc.marketing.marketingsuits.model.root.MarketingSuits;
import com.wanmi.sbc.marketing.marketingsuitssku.model.root.MarketingSuitsSku;
import com.wanmi.sbc.marketing.points.model.root.PointsExchangeActivity;
import com.wanmi.sbc.marketing.points.model.root.PointsExchangeActivityGoods;
import com.wanmi.sbc.marketing.points.repository.PointsExchangeActivityGoodsRepository;
import com.wanmi.sbc.marketing.points.repository.PointsExchangeActivityRepository;
import com.wanmi.sbc.marketing.points.request.PointsExchangeActivityQueryRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PointsExchangeService {

    @Autowired
    private PointsExchangeActivityRepository activityRepository;

    @Autowired
    private PointsExchangeActivityGoodsRepository goodsRepository;


    @Transactional
    public void add(PointsExchangeAcitvityAddRequest request) {

        PointsExchangeActivity activity = KsBeanUtil.convert(request, PointsExchangeActivity.class);
        activity.setCreateTime(LocalDateTime.now());
        activity.setUpdateTime(LocalDateTime.now());
        activity.setDeleted(DeleteFlag.NO.toValue());
        activity.setStatus(0);
        List<PointsExchangeActivityGoods> skus = KsBeanUtil.convert(request.getSkus(), PointsExchangeActivityGoods.class);
        PointsExchangeActivity pointsExchangeActivity = activityRepository.save(activity);
        skus.forEach(sku -> {
            sku.setActivityId(pointsExchangeActivity.getId());
            sku.setCreateTime(LocalDateTime.now());
            sku.setUpdateTime(LocalDateTime.now());
            sku.setDeleted(DeleteFlag.NO.toValue());
        });
        goodsRepository.saveAll(skus);
    }

    public MicroServicePage<PointsExchangeActivityVO> list(PointsExchangePageRequest request){
        PointsExchangeActivityQueryRequest queryRequest = KsBeanUtil.convert(request,PointsExchangeActivityQueryRequest.class);
        Page<PointsExchangeActivity> page = activityRepository.findAll(queryRequest.getWhereCriteria(),queryRequest.getPageRequest());
        return  KsBeanUtil.convertPage(page, PointsExchangeActivityVO.class);
    }

    public PointsExchangeActivityVO detail(Integer id){
        Optional<PointsExchangeActivity> activity = activityRepository.findById(id);
        if(!activity.isPresent()){
            return null;
        }
        PointsExchangeActivityVO activityVO = KsBeanUtil.convert(activity,PointsExchangeActivityVO.class);
        List<PointsExchangeActivityGoods> skus = goodsRepository.getByActId(id);
        if(CollectionUtils.isNotEmpty(skus)){
            activityVO.setSkus(KsBeanUtil.convert(skus, PointsExchangeActivityGoodsVO.class));
        }
        return activityVO;
    }

}
