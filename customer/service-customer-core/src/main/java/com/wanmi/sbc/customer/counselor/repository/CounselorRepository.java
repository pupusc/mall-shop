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
    @Query(value = "select * from t_counselor where user_id = ?1 and status in(0,1)  and del_flag = 1 order by id desc limit 1", nativeQuery = true)
    Counselor getCounselorByUserId(Integer userId);
}
