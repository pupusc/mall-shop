package com.wanmi.sbc.setting.sensitivewords.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.sensitivewords.model.root.SensitiveWords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>DAO</p>
 * @author wjj
 * @date 2019-02-22 16:09:48
 */
@Repository
public interface SensitiveWordsRepository extends JpaRepository<SensitiveWords, Long>,
        JpaSpecificationExecutor<SensitiveWords> {
    /**
     * 批量 逻辑删除敏感词
     * @param deleteFlag
     * @param deleteUser
     * @param deleteTime
     * @param ids
     */
    @Query("update SensitiveWords s set s.delFlag = :deleteFlag,s.deleteUser=:deleteUser,deleteTime=:deleteTime where" +
            " s.sensitiveId in :ids")
    @Modifying
    Integer deleteSensitiveWordsByIds(@Param("deleteFlag") DeleteFlag deleteFlag,@Param("deleteUser") String deleteUser,
                                                   @Param("deleteTime") LocalDateTime deleteTime,@Param("ids") List<Long> ids);


}
