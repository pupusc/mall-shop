package com.wanmi.sbc.elastic.customer.repository;


import com.wanmi.sbc.elastic.customer.model.root.StoreEvaluateSum;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author houshai
 */
public interface EsStoreEvaluateSumRepository extends ElasticsearchRepository<StoreEvaluateSum,Long> {

}