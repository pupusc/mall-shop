package com.wanmi.sbc.goods.tag.repository;

import com.wanmi.sbc.goods.tag.model.TagRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRelRepository extends JpaRepository<TagRel, Long>, JpaSpecificationExecutor<TagRel> {
}
