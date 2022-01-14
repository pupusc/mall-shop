package com.wanmi.sbc.goods.supplier.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.supplier.model.SupplierModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<SupplierModel, Long> {

    List<SupplierModel> findAllByDelFlag(DeleteFlag flag);
}
