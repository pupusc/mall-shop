package com.wanmi.sbc.setting.search.repository;

import com.wanmi.sbc.setting.search.model.SearchAggsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/6 3:09 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Repository
public interface SearchAggsRepository extends JpaRepository<SearchAggsModel, Integer>,
        JpaSpecificationExecutor<SearchAggsModel> {
}
