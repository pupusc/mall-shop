package com.soybean.elastic.booklistmodel.repository;

import com.soybean.elastic.booklistmodel.model.EsBookListModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Description: 书单榜单信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/31 5:23 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Repository
public interface EsBookListModelRepository extends ElasticsearchRepository<EsBookListModel, Long> {
}
