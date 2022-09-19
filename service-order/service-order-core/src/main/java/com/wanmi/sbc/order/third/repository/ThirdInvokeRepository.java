package com.wanmi.sbc.order.third.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.order.third.model.ThirdInvokeDTO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/9/19 2:13 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Repository
public interface ThirdInvokeRepository extends JpaRepository<ThirdInvokeDTO, Integer>, JpaSpecificationExecutor<ThirdInvokeDTO> {


    default Specification<ThirdInvokeDTO> buildSearchCondition(String businessId) {
        return (Specification<ThirdInvokeDTO>) (root, criteriaQuery, criteriaBuilder) -> {
            final List<Predicate> conditionList = new ArrayList<>();
            conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlag.NO));
            if (businessId != null) {
                conditionList.add(criteriaBuilder.equal(root.get("businessId"), businessId));
            }
            return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
        };
    }
}
