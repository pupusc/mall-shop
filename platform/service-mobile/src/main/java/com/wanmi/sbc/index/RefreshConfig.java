package com.wanmi.sbc.index;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * Description: 类的描述
 *
 * @menu: 模块名称
 * Company    : 上海黄豆网络科技有限公司 <br>
 * Author     : tangqinjing<br>
 * Date       : 2021/9/28 15:57<br>
 * Modify     : 修改日期           修改人员        修改说明          JIRA编号<br>
 * v1.0.0       2021/9/28          tangqinjing        新增                   1001<br>
 ********************************************************************/
@Data
@Component
@RefreshScope
public class RefreshConfig {
    @Value("${index.config}")
    private volatile String indexConfig;

    @Value("${ribbon.config}")
    private volatile String ribbonConfig;

    @Value("${activity.start.time}")
    private volatile String activityStartTime;
    @Value("${activity.end.time}")
    private volatile String activityEndTime;

    @Value("${allow.keys}")
    private volatile String allowKeys;

    @Value("${shop.activity.branch.config}")
    private volatile String shopActivityBranchConfig;

    @Value("${shop.activity.branch.top.config}")
    private volatile String shopActivityBranchTopConfig;
}
