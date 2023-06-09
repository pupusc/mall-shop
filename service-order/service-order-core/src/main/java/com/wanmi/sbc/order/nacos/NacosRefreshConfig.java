package com.wanmi.sbc.order.nacos;

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
public class NacosRefreshConfig {

    @Value("${shop.center.feishu.message}")
    private String sendFeiShuMessage;


}
