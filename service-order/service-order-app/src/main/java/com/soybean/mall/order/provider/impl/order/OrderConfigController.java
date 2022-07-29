package com.soybean.mall.order.provider.impl.order;

import com.soybean.mall.order.api.provider.order.OrderConfigProvider;
import com.soybean.mall.order.config.OrderConfigProperties;
import com.soybean.mall.order.gift.service.PayOrderGiftRecordPointService;
import com.soybean.mall.order.gift.service.PayOrderGiftRecordService;
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
    private OrderConfigProperties orderConfigProperties;

    @Override
    public BaseResponse<Map<String, String>> listConfig() {
        Map<String, String> result = new HashMap<>();
        result.put("order.time.out", orderConfigProperties.getTimeOutJson());
        return BaseResponse.success(result);
    }


    /**
     * 这部分代码后续需要删除
     */
    @Autowired
    private PayOrderGiftRecordPointService payOrderGiftRecordPointService;

    @Override
    public BaseResponse list() {
        String message = "{\"channelTypes\":[1],\"orderId\":\"O202207181807389611146\"}";
        payOrderGiftRecordPointService.runTest();
        return BaseResponse.SUCCESSFUL();
    }
}
