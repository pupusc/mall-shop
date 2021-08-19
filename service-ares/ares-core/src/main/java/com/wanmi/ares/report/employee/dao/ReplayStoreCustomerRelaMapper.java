package com.wanmi.ares.report.employee.dao;

import com.wanmi.ares.report.employee.model.root.ReplayStoreCustomerRela;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * ReplayStoreCustomerRelaMapper继承基类
 */
@Repository
public interface ReplayStoreCustomerRelaMapper {

    List<ReplayStoreCustomerRela> selectTotal(Map<String,String> map);


    List<Map<String,Object>> findTotal (Map<String,String> map);
}