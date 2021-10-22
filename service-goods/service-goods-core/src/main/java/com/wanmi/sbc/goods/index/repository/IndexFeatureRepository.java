package com.wanmi.sbc.goods.index.repository;

import com.wanmi.sbc.goods.index.model.IndexFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexFeatureRepository extends JpaRepository<IndexFeature, Long>, JpaSpecificationExecutor<IndexFeature> {
}
