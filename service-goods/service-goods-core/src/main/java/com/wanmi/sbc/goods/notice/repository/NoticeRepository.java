package com.wanmi.sbc.goods.notice.repository;

import com.wanmi.sbc.goods.notice.model.root.NoticeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface NoticeRepository extends JpaRepository<NoticeDTO, Integer>, JpaSpecificationExecutor<NoticeDTO> {
}
