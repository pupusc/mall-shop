package com.wanmi.sbc.setting.qqloginset.repository;

import com.wanmi.sbc.setting.qqloginset.model.root.QqLoginSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * <p>qq登录信息DAO</p>
 * @author lq
 * @date 2019-11-05 16:11:28
 */
@Repository
public interface QqLoginSetRepository extends JpaRepository<QqLoginSet, String>,
        JpaSpecificationExecutor<QqLoginSet> {

}
