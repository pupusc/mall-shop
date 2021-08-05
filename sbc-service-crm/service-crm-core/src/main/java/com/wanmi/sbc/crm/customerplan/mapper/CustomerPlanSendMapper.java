package com.wanmi.sbc.crm.customerplan.mapper;

import com.wanmi.sbc.crm.customerplan.model.CustomerPlanSend;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CustomerPlanSendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CustomerPlanSend record);

    CustomerPlanSend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomerPlanSend record);

    int updateByPrimaryKey(CustomerPlanSend record);

    int insertSelect(CustomerPlanSend record);

    List<CustomerPlanSend> selectByType(CustomerPlanSend record);

    Long selectGiftCount(CustomerPlanSend record);

    /**
     * 运营计划覆盖人数
     * @param planId
     * @return 数量
     */
    Long selectCoversCount(Long planId);

    /**
     * 访客数UV
     * @param planId
     * @return 数量
     */
    Long selectVisitorsUvCount(Long planId);

    /**
     * 访客人数
     * @param planId
     * @return
     */
    Long selectVisitorsCount(Long planId);

    /**
     * 下单人数
     * @param planId
     * @return 数量
     */
    Long selectOrderPersonCount(Long planId);

    /**
     * 下单笔数
     * @param planId
     * @return 数量
     */
    Long selectOrderCount(Long planId);

    /**
     * 付款笔数
     * @param planId
     * @return 数量
     */
    Long selectPayCount(Long planId);

    /**
     * 付款人数
     * @param planId
     * @return 数量
     */
    Long selectPayPersonCount(Long planId);

    /**
     * 付款金额
     * * @param planId
     * @return 数量
     */
    BigDecimal selectTotalPrice(Long planId);



}