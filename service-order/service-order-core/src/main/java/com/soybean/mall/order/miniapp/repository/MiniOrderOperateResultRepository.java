package com.soybean.mall.order.miniapp.repository;

import com.soybean.mall.order.miniapp.model.root.MiniOrderOperateResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;



@Repository
public interface MiniOrderOperateResultRepository extends JpaRepository<MiniOrderOperateResult, Long>,
        JpaSpecificationExecutor<MiniOrderOperateResult> {
    
}