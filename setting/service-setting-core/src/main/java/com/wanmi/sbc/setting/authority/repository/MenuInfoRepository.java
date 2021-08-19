package com.wanmi.sbc.setting.authority.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.setting.authority.model.root.MenuInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统菜单数据源
 * Created by bail on 2017/12/28
 */
@Repository
public interface MenuInfoRepository extends JpaRepository<MenuInfo, String>, JpaSpecificationExecutor<MenuInfo> {

    /**
     * 根据系统类别查询菜单信息
     * @param systemTypeCd 系统类别
     * @param deleteFlag 删除状态
     * @return
     */
    List<MenuInfo> findBySystemTypeCdAndDelFlagOrderBySort(Platform systemTypeCd, DeleteFlag deleteFlag);

    /**
     * 根据菜单id们查询菜单信息List
     * @param menuIdList 菜单id们
     * @param deleteFlag 删除状态
     * @return
     */
    List<MenuInfo> findByMenuIdInAndDelFlagOrderBySort(List<String> menuIdList, DeleteFlag deleteFlag);

    /**
     * 根据id逻辑删除信息
     * @param menuId 主键
     * @param deleteFlag 删除标识
     * @return 条数
     */
    @Modifying
    @Query("update MenuInfo set delFlag = ?2 where menuId = ?1")
    int deleteMenuInfo(String menuId, DeleteFlag deleteFlag);

    /**
     * 根据角色ID查询一级菜单列表
     * @param roleInfoId
     * @return
     */
    @Query("select m.menuName from RoleMenuRela r left join MenuInfo m  on r.menuId = m.menuId where r.roleInfoId = ?1 and  m.menuGrade = 1 and m.delFlag = 0 order by m.sort asc,m.createTime asc ")
    List<String> findMenuNameByRoleInfoId(Long roleInfoId);

}
