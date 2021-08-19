package com.wanmi.sbc.crm.customgroup.mapper;

import com.wanmi.sbc.crm.customgroup.model.CustomGroupParam;
import org.springframework.stereotype.Repository;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-11
 * \* Time: 18:21
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Repository
public interface CustomerTradeStatisticsMapper {

    void delete();

    void save(CustomGroupParam param);
}
