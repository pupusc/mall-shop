package com.wanmi.sbc.goods.freight.repository;

import com.wanmi.sbc.goods.freight.model.root.ExpressNotSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpressNotSupportRepository extends JpaRepository<ExpressNotSupport, Long> {
}
