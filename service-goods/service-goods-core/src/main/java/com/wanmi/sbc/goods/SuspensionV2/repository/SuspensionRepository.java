package com.wanmi.sbc.goods.SuspensionV2.repository;


import com.wanmi.sbc.goods.SuspensionV2.model.Suspension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * <p>悬浮窗DAO</p>
 * @author lws
 * @date 2023-02-4
 */
@Repository
public interface SuspensionRepository extends JpaRepository<Suspension, Long>,
        JpaSpecificationExecutor<Suspension> {

}
