package com.wanmi.sbc.goods.freight.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.freight.model.root.ExpressNotSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpressNotSupportRepository extends JpaRepository<ExpressNotSupport, Long> {

    List<ExpressNotSupport> findAllByDelFlag(DeleteFlag flag);

    List<ExpressNotSupport> findAllBySupplierId(Long supplierId);

    ExpressNotSupport findBySupplierIdAndDelFlag(Long supplierId, DeleteFlag flag);
}
