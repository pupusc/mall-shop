package com.wanmi.sbc.goods.classify.repository;


import com.wanmi.sbc.goods.classify.model.root.ClassifyGoodsRelDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassifyGoodsRelRepository extends JpaRepository<ClassifyGoodsRelDTO, Integer>, JpaSpecificationExecutor<ClassifyGoodsRelDTO> {
}
