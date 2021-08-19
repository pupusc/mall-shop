package com.wanmi.sbc.crm.customgroup.mapper;

import com.wanmi.sbc.crm.api.request.crmgroup.CrmGroupRequest;
import com.wanmi.sbc.crm.customgroup.model.CustomGroupDetail;
import com.wanmi.sbc.crm.customgroup.model.CustomGroupRel;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomGroupRelMapper {
    /**
     * 清空表
     * @return
     */
    int delete();

    /**
     * 根据分群信息统计会员
     * @param customGroupDetail
     */
    void insertBySelect(CustomGroupDetail customGroupDetail);

    /**
     * 根据会员id查询所属自定义分组
     * @param customerId
     * @return
     */
    List<CustomGroupRel> queryListByCustomerId(@Param("customerId")String customerId);

    List<String> queryCustomerPhone(CrmGroupRequest request);

    long queryCustomerPhoneCount(CrmGroupRequest request);

    List<String> queryListByGroupId(CrmGroupRequest request);

}