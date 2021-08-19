package com.wanmi.sbc.elastic.coupon.repository;

import com.wanmi.sbc.elastic.coupon.model.root.EsCouponActivity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 优惠券活动ES持久层
 */
@Repository
public interface EsCouponActivityRepository extends ElasticsearchRepository<EsCouponActivity,String> {
}
