package com.wanmi.sbc.goods.mini.repository.review;

import com.wanmi.sbc.goods.mini.model.review.WxReviewLogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WxReviewLogRepository extends JpaRepository<WxReviewLogModel, Long>, JpaSpecificationExecutor<WxReviewLogModel> {
}
