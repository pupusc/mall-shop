package com.wanmi.sbc.crm.customgroup.mapper;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.crm.api.request.crmgroup.CrmGroupRequest;
import com.wanmi.sbc.crm.api.request.customgroup.CustomGroupListParamRequest;
import com.wanmi.sbc.crm.customgroup.model.CustomGroup;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomGroupMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CustomGroup record);

    int insertSelective(CustomGroup record);

    CustomGroup selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomGroup record);

    int updateByPrimaryKeyWithBLOBs(CustomGroup record);

    int updateByPrimaryKey(CustomGroup record);

    List<CustomGroup> selectList();

    List<CustomGroup> selectListForParam(CustomGroupListParamRequest request);

    List<CustomGroup> selectAll();

    int selectCount();

    int checkCustomerTag(@Param("tagId")Long tagId);

    long countByGroupIds(CrmGroupRequest request);

    long countByPreferenceTags(@Param("tagId")Long tagId);

    long countByAutoTags(@Param("tagId")Long tagId);
}