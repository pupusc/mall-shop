package com.wanmi.sbc.elastic.operationlog.service;

import com.wanmi.sbc.elastic.operationlog.model.root.EsOperationLog;
import com.wanmi.sbc.elastic.operationlog.repository.EsOperationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;


/**
 * @author houshuai
 * @date 2020/12/18 17:16
 * @description <p> 操作日志 Service </p>
 */
@Service
public class EsOperationLogService {

    @Autowired
    private EsOperationLogRepository esOperationLogRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 添加操作日志
     * @param esOperationLog 操作日志实体类
     */
    public void add(EsOperationLog esOperationLog) {
        //手动删除索引时，重新设置mapping
        if(!elasticsearchTemplate.indexExists(EsOperationLog.class)){
            elasticsearchTemplate.createIndex(EsOperationLog.class);
            elasticsearchTemplate.putMapping(EsOperationLog.class);
        }
        esOperationLogRepository.save(esOperationLog);
    }


}
