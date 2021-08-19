package com.wanmi.sbc.elastic.employee.repository;

import com.wanmi.sbc.elastic.employee.model.root.EsEmployee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EsEmployeeRepository extends ElasticsearchRepository<EsEmployee,String> {
}
