package com.wanmi.ares.report.customer.dao;

import com.wanmi.ares.source.model.root.CustomerLevel;
import com.wanmi.ares.source.model.root.ReplayStoreLevel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerLevelMapper {

    /**
     * 主键查询
     *
     * @param levelId
     * @return
     */
    CustomerLevel queryById(@Param("levelId") String levelId);

    /**
     * 主键批量查询
     *
     * @param ids
     * @return
     */
    List<CustomerLevel> queryByIds(List<String> ids);

    /**
     * 主键批量查询品台客户等级
     *
     * @param ids
     * @return
     */
    List<CustomerLevel> queryCustomerLevelByIds(List<String> ids);

    /**
     * @return java.util.List<com.wanmi.ares.source.model.root.ReplayStoreLevel>
     * @Author lvzhenwei
     * @Description 获取店铺等级信息
     * @Date 18:54 2019/9/20
     * @Param [ids]
     **/
    List<ReplayStoreLevel> queryStoreCustomerLevelByIds(List<String> ids);

    /**
     * 新增级别
     *
     * @param customerLevel
     * @return
     */
    int insert(@Param("customerLevel") CustomerLevel customerLevel);

    /**
     * 更新级别信息
     *
     * @param customerLevel
     * @return
     */
    int updateById(@Param("customerLevel") CustomerLevel customerLevel);

    /**
     * 删除级别数据
     *
     * @param levelId
     * @return
     */
    int deleteById(@Param("levelId") String levelId);

    /**
     * 查询所有级别基础数据
     *
     * @return
     */
    List<CustomerLevel> queryAll();

    /**
     * 获取默认等级信息
     *
     * @return
     */
    CustomerLevel getDefaultLevel();

}

