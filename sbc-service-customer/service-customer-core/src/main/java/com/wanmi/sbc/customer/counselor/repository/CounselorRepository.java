package com.wanmi.sbc.customer.counselor.repository;

import com.wanmi.sbc.customer.counselor.model.root.Counselor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * <p>用户签到记录DAO</p>
 * @author wangtao
 * @date 2019-10-05 16:13:04
 */
@Repository
public interface CounselorRepository extends JpaRepository<Counselor, String>,
        JpaSpecificationExecutor<Counselor> {

    /**
     * 通过用户获取知识顾问信息
     * @param userId
     * @return
     */
    @Query("from Counselor where userId = ?1 and status = 2 and delFlag = 1")
    Counselor getCounselorByUserId(Integer userId);
}
