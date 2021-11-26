package com.wanmi.sbc.crm.tagparam.repository;

import com.wanmi.sbc.crm.tagparam.model.root.TagParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <p>标签参数DAO</p>
 * @author dyt
 * @date 2020-03-12 15:59:49
 */
@Repository
public interface TagParamRepository extends JpaRepository<TagParam, Long>,
        JpaSpecificationExecutor<TagParam> {

    void deleteByIdIn(List<Long> ids);

    List<TagParam> findByIdIn(List<Long> ids);
}
