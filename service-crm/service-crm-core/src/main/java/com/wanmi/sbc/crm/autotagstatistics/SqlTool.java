package com.wanmi.sbc.crm.autotagstatistics;

import com.wanmi.sbc.crm.bean.enums.DimensionName;
import org.springframework.stereotype.Component;

/**
 * @program: sbc-micro-service-A
 * @description: sql拼接
 * @create: 2020-08-28 11:38
 **/
@Component
public abstract class SqlTool {

    public abstract DimensionName[] supports();

    public abstract String getSql(StatisticsTagInfo tagInfo);
}