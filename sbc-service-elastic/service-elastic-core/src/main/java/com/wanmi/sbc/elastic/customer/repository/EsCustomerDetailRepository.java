package com.wanmi.sbc.elastic.customer.repository;

import com.wanmi.sbc.elastic.customer.model.root.EsCustomerDetail;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 会员详情持久层
 */
@Repository
public interface EsCustomerDetailRepository extends ElasticsearchRepository<EsCustomerDetail,String> {

}
