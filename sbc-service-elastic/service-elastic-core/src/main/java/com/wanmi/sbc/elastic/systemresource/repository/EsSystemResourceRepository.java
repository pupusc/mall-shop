package com.wanmi.sbc.elastic.systemresource.repository;

import com.wanmi.sbc.elastic.systemresource.model.root.EsSystemResource;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author houshuai
 * @date 2020/12/14 11:01
 * @description <p> </p>
 */
public interface EsSystemResourceRepository extends ElasticsearchRepository<EsSystemResource,Long> {
}
