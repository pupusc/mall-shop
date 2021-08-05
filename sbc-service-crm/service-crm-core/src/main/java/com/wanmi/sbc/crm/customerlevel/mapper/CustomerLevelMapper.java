package com.wanmi.sbc.crm.customerlevel.mapper;

import com.wanmi.sbc.crm.customerlevel.model.CustomerLevel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-13
 * \* Time: 16:28
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Repository
public interface CustomerLevelMapper {

    List<CustomerLevel> queryList(CustomerLevel customerLevel);

    int queryCount(CustomerLevel customerLevel);
}
