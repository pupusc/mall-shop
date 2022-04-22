package com.soybean.mall.wx.mini.order.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/4/22 12:52 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class WxOrderCancelRequest implements Serializable {

    /**
     * 微信订单号
     */
    @JSONField(name = "order_id")
    private String orderId;

    /**
     * 系统订单号
     */
    @JSONField(name = "out_order_id")
    private String outOrderId;

    /**
     *
     */
    @JSONField(name = "openid")
    private String openid;
}
