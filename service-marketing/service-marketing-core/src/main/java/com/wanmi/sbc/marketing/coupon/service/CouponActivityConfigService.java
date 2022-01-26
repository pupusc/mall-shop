package com.wanmi.sbc.marketing.coupon.service;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.request.store.ListStoreByIdsRequest;
import com.wanmi.sbc.customer.api.response.store.ListStoreByIdsResponse;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.marketing.bean.constant.Constant;
import com.wanmi.sbc.marketing.bean.enums.ScopeType;
import com.wanmi.sbc.marketing.bean.vo.CouponCodeVO;
import com.wanmi.sbc.marketing.coupon.model.root.CouponActivityConfig;
import com.wanmi.sbc.marketing.coupon.model.entity.cache.CouponCache;
import com.wanmi.sbc.marketing.coupon.model.root.CouponMarketingScope;
import com.wanmi.sbc.marketing.coupon.request.CouponCacheInitRequest;
import com.wanmi.sbc.marketing.coupon.repository.CouponActivityConfigRepository;
import com.wanmi.sbc.marketing.coupon.request.CouponCodePageRequest;
import com.wanmi.sbc.marketing.coupon.request.CouponQueryRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicQueryRequest;
import com.wanmi.sbc.setting.bean.vo.TopicConfigVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 优惠券活动配置
 */
@Service
public class CouponActivityConfigService {

    @Autowired
    private CouponActivityConfigRepository couponActivityConfigRepository;

    @Autowired
    private EntityManager entityManager;

    /**
     * 插入活动配置信息
     */
    @Transactional
    public void insertCouponActivityConfig(CouponActivityConfig couponActivityConfig) {
        couponActivityConfigRepository.save(couponActivityConfig);
    }


    /**
     * 根据优惠券id获取活动配置信息
     *
     * @param couponIds 优惠券id
     * @return
     */
    public List<CouponActivityConfig> queryByCouponIds(List<String> couponIds) {
        return couponActivityConfigRepository.findByCouponIds(couponIds);
    }

    /**
     * 根据优惠券id获取活动配置信息
     *
     * @param couponId
     * @return
     */
    public List<CouponActivityConfig> findByCouponId(String couponId) {
        return couponActivityConfigRepository.findByCouponId(couponId);
    }

    /**
     * 根据活动id获取活动配置信息
     *
     * @param activityId 活动id
     * @return
     */
    public List<CouponActivityConfig> queryByActivityId(String activityId) {
        return couponActivityConfigRepository.findByActivityId(activityId);
    }

    /**
     * 根据活动id和优惠券id，查询具体规则
     *
     * @param activityId
     * @param couponId
     * @return
     */
    public CouponActivityConfig queryByActivityIdAndCouponId(String activityId, String couponId) {
        return couponActivityConfigRepository.findByActivityIdAndCouponId(activityId, couponId);
    }

    /**
     * 根据主键更新信息
     *
     * @param couponActivityConfig
     */
    @Transactional
    public void updateByPk(CouponActivityConfig couponActivityConfig) {
        couponActivityConfigRepository.save(couponActivityConfig);
    }


    /**
     * 获取已经开始的优惠券活动信息
     *
     * @param request
     * @return
     */
    public List<CouponCache> queryCouponStarted(CouponCacheInitRequest request) {
        Query query = entityManager.createNativeQuery(request.getQuerySql());
        if (CollectionUtils.isNotEmpty(request.getCouponActivityIds())) {
            query.setParameter("couponActivityIds", request.getCouponActivityIds());
        }
        if (request.getQueryStartTime() != null) {
            query.setParameter("queryStartTime", DateUtil.format(request.getQueryStartTime(), DateUtil.FMT_TIME_1));
        }
        if (request.getQueryEndTime() != null) {
            query.setParameter("queryEndTime", DateUtil.format(request.getQueryEndTime(), DateUtil.FMT_TIME_1));
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return CouponCacheInitRequest.converter(query.getResultList());
    }

    /**
     * 查询生效中和未开始的优惠券活动信息
     * @param request
     * @return
     */
    public Page<CouponCache> queryCouponStartedAndUnStart(CouponQueryRequest request) {
        Query query = entityManager.createNativeQuery(request.getQuerySql());
        query.setFirstResult(request.getPageNum() * request.getPageSize());
        query.setMaxResults(request.getPageSize());
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<CouponCache> couponCaches =  CouponQueryRequest.converter(query.getResultList());

        //查询优惠券总数
        Query totalCountRes = entityManager.createNativeQuery(request.getQueryTotalCountSql());
        long totalCount = Long.parseLong(totalCountRes.getSingleResult().toString());
        return new PageImpl<>(couponCaches, request.getPageable(), totalCount);
    }


}
