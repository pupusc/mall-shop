package com.wanmi.sbc.setting.popupadministration.repository;


import com.wanmi.sbc.setting.popupadministration.model.ApplicationPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 弹窗管理
 */
@Repository
public interface ApplicationPageRepository extends JpaRepository<ApplicationPage,Long>,
        JpaSpecificationExecutor<ApplicationPage> {


    @Modifying
    @Query(value = "update ApplicationPage at set at.sortNumber =:sortNumber where at" +
            ".popupId=:popupId and at.applicationPageName=:applicationPageName")
     Integer sortPopupAdministration(@Param("applicationPageName") String applicationPageName,
                                     @Param("popupId") Long popupId, @Param("sortNumber") Long sortNumber);

    /**
     * 根据页面名称搜索相关的弹窗
     */
    List<ApplicationPage> findAllByApplicationPageNameContainingOrderBySortNumberAsc(String associationalWordNam);


    Integer deleteByPopupId(Long popupId);

}
