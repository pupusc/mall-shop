package com.wanmi.sbc.goods.ruleinfo.repository;

import com.wanmi.sbc.goods.ruleinfo.model.root.RuleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.util.Optional;
import java.util.List;

/**
 * <p>规则说明DAO</p>
 * @author zxd
 * @date 2020-05-25 18:55:56
 */
@Repository
public interface RuleInfoRepository extends JpaRepository<RuleInfo, Long>,
        JpaSpecificationExecutor<RuleInfo> {

    /**
     * 单个删除规则说明
     * @author zxd
     */
    @Modifying
    @Query("update RuleInfo set delFlag = 1 where id = ?1")
    void deleteById(Long id);

    /**
     * 批量删除规则说明
     * @author zxd
     */
    @Modifying
    @Query("update RuleInfo set delFlag = 1 where id in ?1")
    void deleteByIdList(List<Long> idList);

    Optional<RuleInfo> findByIdAndDelFlag(Long id, DeleteFlag delFlag);

}
