package com.wanmi.sbc.goods.index.repository;


import com.wanmi.sbc.goods.index.model.NormalModuleSku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NormalModuleSkuRepository extends JpaRepository<NormalModuleSku, Integer>, JpaSpecificationExecutor<NormalModuleSku> {
}
