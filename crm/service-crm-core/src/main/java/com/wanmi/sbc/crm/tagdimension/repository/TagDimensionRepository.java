package com.wanmi.sbc.crm.tagdimension.repository;

import com.wanmi.sbc.crm.tagdimension.model.root.TagDimension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <p>标签维度DAO</p>
 * @author dyt
 * @date 2020-03-12 16:00:30
 */
@Repository
public interface TagDimensionRepository extends JpaRepository<TagDimension, Long>,
        JpaSpecificationExecutor<TagDimension> {

    void deleteByIdIn(List<Long> ids);

    List<TagDimension> findByIdIn(List<Long> ids);
}
