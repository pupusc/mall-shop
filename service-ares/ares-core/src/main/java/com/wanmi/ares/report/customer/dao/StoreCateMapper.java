package com.wanmi.ares.report.customer.dao;

import com.wanmi.ares.source.model.root.Store;
import com.wanmi.ares.source.model.root.StoreCate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreCateMapper {

    int insert(@Param("storeCate") StoreCate storeCate);

    Store queryById(@Param("storeCateId") String storeCateId);

    int updateById(@Param("storeCate") StoreCate storeCate);

    int deleteByIds(@Param("ids") List<String> ids);

    List<StoreCate> queryByParentId(@Param("cateParentIds") List<String> cateParentIds);

    List<StoreCate> queryByIds(@Param("ids") List<String> storeCateIds);
}

