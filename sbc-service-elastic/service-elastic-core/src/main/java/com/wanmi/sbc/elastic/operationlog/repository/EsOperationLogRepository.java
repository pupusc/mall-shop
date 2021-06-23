package com.wanmi.sbc.elastic.operationlog.repository;

import com.wanmi.sbc.elastic.operationlog.model.root.EsOperationLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author houshuai
 * @date 2020/12/16 10:28
 * @description <p> </p>
 */
public interface EsOperationLogRepository extends ElasticsearchRepository<EsOperationLog, Long> {

}

