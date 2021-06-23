package com.wanmi.sbc.setting.syssms.repository;

import com.wanmi.sbc.setting.syssms.model.root.SysSms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * <p>系统短信配置DAO</p>
 * @author lq
 * @date 2019-11-05 16:13:47
 */
@Repository
public interface SysSmsRepository extends JpaRepository<SysSms, String>,
        JpaSpecificationExecutor<SysSms> {

}
