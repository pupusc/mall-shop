package com.wanmi.sbc.setting.authority.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.setting.authority.model.root.FunctionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 系统功能数据源
 * Created by bail on 2017/01/04
 */
@Repository
public interface FunctionRepository extends JpaRepository<FunctionInfo, String>, JpaSpecificationExecutor<FunctionInfo>{

    /**
     * 根据系统类别查询菜单信息
     * @param systemTypeCd 系统类别
     * @param deleteFlag 删除状态
     * @return
     */
    List<FunctionInfo> findBySystemTypeCdAndDelFlagOrderBySort(Platform systemTypeCd, DeleteFlag deleteFlag);

    /**
     * 根据id逻辑删除信息
     * @param functionId 主键
     * @param deleteFlag 删除标识
     * @return 条数
     */
    @Modifying
    @Query("update FunctionInfo set delFlag = ?2 where functionId = ?1")
    int deleteFunctionInfo(String functionId, DeleteFlag deleteFlag);
}
