package com.soybean.mall.order.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * Description: 统一配置
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/21 2:24 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Configuration
@RefreshScope
@Data
public class OrderConfigProperties {

    /**
     * 订单超时时间（微信）
     */
    @Value("${order.time.out}")
    private String timeOutJson;
}
