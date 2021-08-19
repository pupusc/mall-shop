package com.wanmi.sbc.crm.autotagsql.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.crm.autotagsql.model.root.AutoTagSql;
import com.wanmi.sbc.crm.tagparam.model.root.TagParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>标签参数DAO</p>
 * @author dyt
 * @date 2020-03-12 15:59:49
 */
@Repository
public interface AutoTagSqlRepository extends JpaRepository<AutoTagSql, Long>,
        JpaSpecificationExecutor<TagParam> {
    AutoTagSql findByAutoTagIdAndDelFlag(Long AutoTagId, DeleteFlag deleteFlag);

    void deleteAutoTagSqlByAutoTagId(Long autoTagId);

    void deleteAutoTagSqlsByAutoTagIdIn(List<Long> ids);
}
