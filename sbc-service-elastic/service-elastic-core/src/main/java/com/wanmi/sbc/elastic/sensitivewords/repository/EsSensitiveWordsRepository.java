package com.wanmi.sbc.elastic.sensitivewords.repository;

import com.wanmi.sbc.elastic.sensitivewords.model.root.EsSensitiveWords;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author houshuai
 * @date 2020/12/11 16:25
 * @description <p> </p>
 */
public interface EsSensitiveWordsRepository extends ElasticsearchRepository<EsSensitiveWords,Long> {

}
