package com.soybean.elastic.spu.repository;

import com.soybean.elastic.spu.model.EsSpuNew;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/29 1:27 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Repository
public interface EsSpuNewRepository extends ElasticsearchRepository<EsSpuNew, String> {


}
