package com.wanmi.sbc.goods.marketing.service;


//import com.codingapi.txlcn.tc.annotation.TccTransaction;

import com.wanmi.sbc.common.util.UUIDUtil;
import com.wanmi.sbc.goods.marketing.model.data.GoodsMarketing;
import com.wanmi.sbc.goods.marketing.repository.GoodsMarketingRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 商品营销
 */
@Service
@Transactional(readOnly = true, timeout = 10, rollbackFor = Exception.class)
public class GoodsMarketingService {
    @Autowired
    private GoodsMarketingRepository marketingRepository;

    @Autowired
    private GoodsMarketingService goodsMarketingService;

    /**
     * 新增文档
     * 专门用于数据新增服务,不允许数据修改的时候调用
     *
     * @param goodsMarketing
     */
    public GoodsMarketing addGoodsMarketing(GoodsMarketing goodsMarketing) {
        return marketingRepository.save(goodsMarketing);
    }

    /**
     * 修改文档
     * 专门用于数据修改服务,不允许数据新增的时候调用
     *
     * @param goodsMarketing
     */
    public GoodsMarketing updateGoodsMarketing(GoodsMarketing goodsMarketing) {
        return marketingRepository.save(goodsMarketing);
    }

    /**
     * 获取采购单中, 各商品用户选择/默认选择参加的营销活动(满减/满折/满赠)
     *
     * @param customerId
     * @return
     */
    public List<GoodsMarketing> queryGoodsMarketingList(String customerId) {
        List<GoodsMarketing> goodsMarketings = marketingRepository.queryGoodsMarketingListByCustomerId(customerId);
        // 这边是为了处理用户高并发设置sku对应的营销活动,mongo中数据出现重复,采购单加载列表失败的问题(若迁移mysql加约束即可)
        //去重处理
        return goodsMarketings.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                new TreeSet<>(Comparator.comparing(o -> o.getCustomerId() + ":" + o.getGoodsInfoId() + ":" +o.getMarketingId()))
        ), ArrayList::new));
    }

    /**
     * 根据用户编号删除商品使用的营销
     *
     * @param customerId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int delByCustomerId(String customerId) {
        return marketingRepository.deleteAllByCustomerId(customerId);
    }

    /**
     * 根据用户编号和商品编号列表删除商品使用的营销
     *
     * @param customerId
     * @param goodsInfoIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int delByCustomerIdAndGoodsInfoIds(String customerId, List<String> goodsInfoIds) {
        return marketingRepository.deleteByCustomerIdAndGoodsInfoIdIn(customerId, goodsInfoIds);
    }

    /**
     * 批量添加商品使用的营销
     *
     * @param goodsMarketings
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<GoodsMarketing> batchAdd(List<GoodsMarketing> goodsMarketings) {
        return marketingRepository.saveAll(goodsMarketings);
    }


    /**
     * 修改商品使用的营销
     *
     * @param goodsMarketing
     * @return
     */
    //@TccTransaction
    @Transactional
    public GoodsMarketing modify(GoodsMarketing goodsMarketing) {
        if (StringUtils.isEmpty(goodsMarketing.getId())) {
            goodsMarketing.setId(UUIDUtil.getUUID());
        }
        List<GoodsMarketing> oldGoodsMarketingList = marketingRepository.queryByCustomerIdAndGoodsInfoId(goodsMarketing.getCustomerId(), goodsMarketing.getGoodsInfoId());

        if (CollectionUtils.isEmpty(oldGoodsMarketingList)) {
            return goodsMarketingService.addGoodsMarketing(goodsMarketing);
        }
        oldGoodsMarketingList.stream().forEach(m->{
            m.setMarketingId(goodsMarketing.getMarketingId());
        });

        return goodsMarketingService.batchAdd(oldGoodsMarketingList).get(oldGoodsMarketingList.size()-1);
    }
}
