package com.wanmi.sbc.crm.autotag.repository;

import com.wanmi.sbc.crm.autotag.model.root.AutoTagInit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>自动标签DAO</p>
 * @author dyt
 * @date 2020-03-11 14:47:32
 */
@Repository
public interface AutoTagInitRepository extends JpaRepository<AutoTagInit, Long>{

    List<AutoTagInit> findByIdIn(List<Long> ids);
}
