package com.wanmi.sbc.goods.image.repository;

import com.wanmi.sbc.goods.image.model.root.ImageDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ImageRepository extends JpaRepository<ImageDTO, Integer>, JpaSpecificationExecutor<ImageDTO> {
}
