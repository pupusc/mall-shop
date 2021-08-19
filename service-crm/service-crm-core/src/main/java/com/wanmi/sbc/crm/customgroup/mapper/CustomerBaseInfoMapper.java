package com.wanmi.sbc.crm.customgroup.mapper;

import com.wanmi.sbc.crm.customgroup.model.CustomerBaseInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CustomerBaseInfoDAO继承基类
 */
@Repository
public interface CustomerBaseInfoMapper{

    void delete();

    void save();

    List<String> selectPhoneByCustomerId(@Param("idList") List<String> idList);
}