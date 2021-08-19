package com.wanmi.sbc.marketing.coupon.service;

import com.wanmi.sbc.marketing.coupon.model.root.CouponMarketingScope;
import com.wanmi.sbc.marketing.coupon.repository.CouponMarketingScopeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 优惠券商品作用范围
 */
@Service
public class CouponMarketingScopeService {

    @Autowired
    private CouponMarketingScopeRepository couponMarketingScopeRepository;

    /**
     * 插入优惠券商品作用范围
     */
    @Transactional
    public void insertCouponMarketingScope(CouponMarketingScope couponMarketingScope) {

         couponMarketingScopeRepository.save(couponMarketingScope);
    }

    /**
     * 按照优惠券id查询作用范围
     * @param couponId
     * @return
     */
    public List<CouponMarketingScope> listScopeByCouponId(String couponId){
        return couponMarketingScopeRepository.findByCouponId(couponId);
    }

    /**
     * 按照优惠券id集合查询作用范围
     * @param couponIds
     * @return 集合
     */
    public List<CouponMarketingScope> listScopeByCouponIds(List<String> couponIds){
        return couponMarketingScopeRepository.findByCouponIdIn(couponIds);
    }

    /**
     * 按照优惠券id集合查询作用范围
     * @param couponIds
     * @return <CouponId, 集合>
     */
    public Map<String, List<CouponMarketingScope>> mapScopeByCouponIds(List<String> couponIds){
        List<CouponMarketingScope> scopeList = couponMarketingScopeRepository.findByCouponIdIn(couponIds);
        return scopeList.stream().collect(Collectors.groupingBy(CouponMarketingScope::getCouponId));
    }

    /**
     * 按照优惠券范围id查询作用范围的优惠券
     * @param scopeId
     * @return
     */
    public List<CouponMarketingScope> listScopeByScopeId(String scopeId){
        return couponMarketingScopeRepository.findByScopeId(scopeId);
    }

    /**
     * 批量插入优惠券商品作用范围
     * @param couponMarketingScopes
     */
    @Transactional
    public void addBatchCouponMarketingScope(List<CouponMarketingScope> couponMarketingScopes) {

        couponMarketingScopeRepository.saveAll(couponMarketingScopes);
    }

}
