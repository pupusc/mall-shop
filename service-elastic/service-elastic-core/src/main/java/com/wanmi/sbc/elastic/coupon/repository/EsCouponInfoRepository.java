package com.wanmi.sbc.elastic.coupon.repository;


import com.wanmi.sbc.elastic.coupon.model.root.EsCouponInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 优惠券ES持久层
 */
@Repository
public interface EsCouponInfoRepository extends ElasticsearchRepository<EsCouponInfo,String> {

}