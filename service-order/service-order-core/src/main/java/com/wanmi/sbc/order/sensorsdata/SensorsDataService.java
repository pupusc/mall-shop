package com.wanmi.sbc.order.sensorsdata;

import com.sensorsdata.analytics.javasdk.bean.EventRecord;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/28 4:56 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class SensorsDataService {

    /**
     * 基础信息
     * @param eventName
     * @return
     */
    private EventRecord.Builder addBaseEventRecord(String eventName) {
        return EventRecord.builder()
                .isLoginId(Boolean.FALSE)
                .setEventName(eventName)
                .addProperty("$platform_type", "H5")
                .addProperty("to_sensors", "1"); //表示的是商城

    }

    /**
     * 支付成功埋点
     * @param distinctId
     * @param goodsInfoId
     * @return
     */
    public EventRecord.Builder addPaySuccessEventRecord(String distinctId, String goodsInfoId, String price) {

        EventRecord.Builder builder = this.addBaseEventRecord("shop_pay_0_success");
        builder.setDistinctId(distinctId)
                .addProperty("click_type", "付款成功")
                .addProperty("var_id", goodsInfoId)
                .addProperty("price", price);
        return builder;
    }

}
