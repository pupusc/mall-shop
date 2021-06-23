package com.wanmi.sbc.setting.authority.repository;

import com.wanmi.sbc.setting.authority.model.pk.RoleFunctionRelaPK;
import com.wanmi.sbc.setting.authority.model.root.RoleFunctionRela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色-功能关联信息数据源
 * Created by bail on 2017/01/04
 */
@Repository
public interface RoleFunctionRelaRepository extends JpaRepository<RoleFunctionRela, RoleFunctionRelaPK>, JpaSpecificationExecutor<RoleFunctionRela>{

    /**
     * 根据角色标识查询拥有的功能信息
     * @param roleInfoId
     * @return
     */
    List<RoleFunctionRela> findByRoleInfoId(Long roleInfoId);

    /**
     * 根据角色标识删除拥有的功能信息
     * @param roleInfoId
     */
    void deleteByRoleInfoId(Long roleInfoId);

    /**
     * 查询某角色的所有权限
     */
    @Query(value = "SELECT a.authority_url,a.request_type FROM role_function_rela r JOIN authority a ON r.function_id = a.function_id WHERE r.role_info_id = ?1", nativeQuery = true)
    List<Object> hasAuthorityList(Long roleInfoId);

    /**
     * 根据角色/功能名称list查询拥有的功能名称list
     */
    @Query(value = "SELECT a.function_name FROM role_function_rela r JOIN function_info a ON r.function_id = a.function_id WHERE r.role_info_id = ?1 AND a.function_name IN ?2 GROUP BY a.function_name", nativeQuery = true)
    List<Object> hasFunctionList(Long roleInfoId, List<String> authorityNames);

    /**
     * 只根据角色 查询功能名称List
     */
    @Query(value = "SELECT a.function_name FROM role_function_rela r JOIN function_info a ON r.function_id = a.function_id WHERE r.role_info_id = ?1 GROUP BY a.function_name", nativeQuery = true)
    List<Object> queryFunctionsByRoleId(Long roleInfoId);

}
