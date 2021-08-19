package com.wanmi.ares.report.customer.dao;

import com.wanmi.ares.source.model.root.Store;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ReplayStoreMapper {


    Store queryById(@Param("storeId") String storeId);

    Store queryByCompanyInfoId(@Param("companyInfoId") Long companyInfoId);

    List<Store> queryByCompanyIds(@Param("companyIds") List<String> companyIds);

    List<Store> queryByCondition(Map<String, Object> params);


    /**
     * 查询所有店铺数据
     * @return
     */
    @Select("select company_info_id as companyInfoId,store_name as storeName,supplier_name as supplierName,store_state " +
            "as storeState,contract_start_date as contractStartDate,contract_end_date as contractEndDate" +
            " from replay_store limit 500000")
    List<Store> queryAllStore();

    /**
     * 查询所有店铺数据
     * @return
     */
    @Select("select * from replay_store where del_flag = #{delFlag}")
    @ResultMap("storeMap")
    List<Store> queryAllStoreByFlag(@Param("delFlag") boolean delFlag);

    @Select("select count(id) from replay_store where del_flag = #{delFlag}")
    int queryCountStore(@Param("delFlag") boolean delFlag);

    /**
     * 查询店铺名称
     * @return 店铺名称
     */
    @Select("select store_name from replay_store where company_info_id = #{companyId}")
    String findCompanyName(@Param("companyId") String companyId);


}

