package com.wanmi.sbc.goods.classify.repository;


import com.wanmi.sbc.goods.classify.model.root.ClassifyDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ClassifyRepository extends JpaRepository<ClassifyDTO, Integer>, JpaSpecificationExecutor<ClassifyDTO> {
}
