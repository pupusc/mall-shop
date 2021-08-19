package com.wanmi.sbc.setting.authority.repository;

import com.wanmi.sbc.setting.authority.model.pk.RoleMenuRelaPK;
import com.wanmi.sbc.setting.authority.model.root.RoleMenuRela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色-菜单信息关联数据源
 * Created by bail on 2017/12/28
 */
@Repository
public interface RoleMenuRelaRepository extends JpaRepository<RoleMenuRela, RoleMenuRelaPK>, JpaSpecificationExecutor<RoleMenuRela>{

    /**
     * 根据角色标识查询拥有的菜单信息
     * @param roleInfoId
     * @return
     */
    List<RoleMenuRela> findByRoleInfoId(Long roleInfoId);

    /**
     * 根据角色标识删除拥有的菜单信息
     * @param roleInfoId
     */
    void deleteByRoleInfoId(Long roleInfoId);

    /**
     * 根据角色标识查询拥有的菜单信息
     * @param roleInfoId
     * @return
     */
    List<RoleMenuRela> findByRoleInfoIdIn(List<Long> roleInfoId);

}
