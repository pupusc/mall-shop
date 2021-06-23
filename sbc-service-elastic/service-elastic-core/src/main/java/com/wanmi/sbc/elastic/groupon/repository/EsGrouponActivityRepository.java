package com.wanmi.sbc.elastic.groupon.repository;

import com.wanmi.sbc.elastic.groupon.model.root.EsGrouponActivity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author: HouShuai
 * @date: 2020/12/8 11:16
 * @description:
 */
public interface EsGrouponActivityRepository extends ElasticsearchRepository<EsGrouponActivity, String> {

}
