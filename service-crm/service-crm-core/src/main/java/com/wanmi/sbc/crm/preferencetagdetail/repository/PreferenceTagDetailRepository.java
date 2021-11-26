package com.wanmi.sbc.crm.preferencetagdetail.repository;

import com.wanmi.sbc.crm.preferencetagdetail.model.root.PreferenceTagDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <p>偏好标签明细DAO</p>
 * @author dyt
 * @date 2020-03-11 14:58:07
 */
@Repository
public interface PreferenceTagDetailRepository extends JpaRepository<PreferenceTagDetail, Long>,
        JpaSpecificationExecutor<PreferenceTagDetail> {

    void deleteByIdIn(List<Long> ids);

    void deleteByTagId(Long id);

    int countAllByIdIn(List<Long> ids);
}
