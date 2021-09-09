package com.wanmi.sbc.goods.prop.repository;

import com.wanmi.sbc.goods.prop.model.root.GoodsPropCateRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsPropCateRelRepository extends JpaRepository<GoodsPropCateRel, Long>, JpaSpecificationExecutor<GoodsPropCateRel> {


}
