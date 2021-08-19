package com.wanmi.sbc.setting.popupadministration.repository;


import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.popupadministration.model.PopupAdministration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 弹窗管理
 */
@Repository
public interface PopupAdministrationRepository extends JpaRepository<PopupAdministration,Long>,
        JpaSpecificationExecutor<PopupAdministration> {

    /**
     * 删除弹窗
     * @param popupId
     * @return
     */
    @Modifying
    @Query("update PopupAdministration set delFlag = 1 where popupId = :popupId")
    int deletePopupAdministration(@Param("popupId") Long popupId);

    /**
     * 暂停弹窗  startPopupAdministration
     * @param popupId
     * @return
     */
    @Modifying
    @Query("update PopupAdministration set isPause = 1 where popupId = :popupId")
    int pausePopupAdministration(@Param("popupId") Long popupId);

    /**
     * 启动弹窗
     * @param popupId
     * @return
     */
    @Modifying
    @Query("update PopupAdministration set isPause = 0 where popupId = :popupId")
    int startPopupAdministration(@Param("popupId") Long popupId);

    /**
     * 根据页面名称搜索相关的弹窗
     */
    PopupAdministration findAllByPopupIdAndDelFlag(Long popupIds, DeleteFlag deleteFlag);

}
