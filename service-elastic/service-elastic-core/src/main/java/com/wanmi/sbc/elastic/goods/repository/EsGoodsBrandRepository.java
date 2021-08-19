package com.wanmi.sbc.elastic.goods.repository;

import com.wanmi.sbc.elastic.goods.model.root.EsGoodsBrand;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author houshuai
 * @date 2020/12/10 14:45
 * @description <p> </p>
 */
public interface EsGoodsBrandRepository extends ElasticsearchRepository<EsGoodsBrand,Long> {
}
