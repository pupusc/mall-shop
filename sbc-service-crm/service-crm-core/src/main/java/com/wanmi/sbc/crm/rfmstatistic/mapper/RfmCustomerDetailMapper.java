package com.wanmi.sbc.crm.rfmstatistic.mapper;

import com.wanmi.sbc.crm.entity.BaseParam;
import com.wanmi.sbc.crm.rfmstatistic.model.RfmCustomerDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-10-12
 * \* Time: 14:52
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Mapper
public interface RfmCustomerDetailMapper {


    void saveRValues(BaseParam baseParam);

    void saveFAndMValues(BaseParam baseParam);

    void deleteByDate(@Param("statDate")String statDate);

    List<RfmCustomerDetail> queryAll(RfmCustomerDetail rfmCustomerDetail);

    Long queryCount(RfmCustomerDetail rfmCustomerDetail);

    void batchUpdateScore(List<RfmCustomerDetail> list);

    void updateCustomerSystemGroupId(@Param("statDate")String statDate);

    RfmCustomerDetail queryCustomerDetail(@Param("customerId")String customerId);

    RfmCustomerDetail getCustomerDetail(@Param("customerId")String customerId);

}
