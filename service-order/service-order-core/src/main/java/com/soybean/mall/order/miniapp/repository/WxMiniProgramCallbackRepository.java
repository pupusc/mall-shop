package com.soybean.mall.order.miniapp.repository;

import com.soybean.mall.order.miniapp.model.root.WxMiniProgramCallbackModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface WxMiniProgramCallbackRepository extends JpaRepository<WxMiniProgramCallbackModel, String>, JpaSpecificationExecutor<WxMiniProgramCallbackModel> {

    @Transactional
    @Modifying
    @Query(value = "update t_mini_program_callback set status=?1,update_time=now() where id=?2", nativeQuery = true)
    int updateStatus(int status, Long id);
}
