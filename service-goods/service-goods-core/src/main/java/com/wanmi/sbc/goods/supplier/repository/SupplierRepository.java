package com.wanmi.sbc.goods.supplier.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.index.model.IndexFeature;
import com.wanmi.sbc.goods.supplier.model.SupplierModel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<SupplierModel, Long>, JpaSpecificationExecutor<SupplierModel> {

    List<SupplierModel> findAllByDelFlag(DeleteFlag flag);

    SupplierModel findByCodeAndDelFlag(String code, DeleteFlag delFlag);

    default Specification<SupplierModel> buildQueryCondition() {
        return (Specification<SupplierModel>) (root, criteriaQuery, criteriaBuilder) -> {
            final List<Predicate> conditionList = new ArrayList<>();
            //只是获取有效的
            conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlag.NO));
            return criteriaBuilder.and(conditionList.toArray(new Predicate[0]));
        };
    }
}
