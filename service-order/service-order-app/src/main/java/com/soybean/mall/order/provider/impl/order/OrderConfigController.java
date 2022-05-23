package com.soybean.mall.order.provider.impl.order;

import com.soybean.mall.order.api.provider.order.OrderConfigProvider;
import com.soybean.mall.order.config.ConfigProperties;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/24 12:44 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
public class OrderConfigController implements OrderConfigProvider {

    @Autowired
    private ConfigProperties configProperties;

    @Override
    public BaseResponse<Map<String, String>> listConfig() {
        Map<String, String> result = new HashMap<>();
        result.put("order.time.out", configProperties.getTimeOutJson());
        return BaseResponse.success(result);
    }
}
