package com.wanmi.sbc.goods.chooserule.repository;

import com.wanmi.sbc.goods.chooserule.model.root.ChooseRuleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/1 8:15 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Repository
public interface ChooseRuleRepository extends JpaRepository<ChooseRuleDTO, Integer>, JpaSpecificationExecutor<ChooseRuleDTO> {
}
