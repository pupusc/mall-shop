package com.soybean.mall.wx.mini.order.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * Description: 更新售后订单
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/6 2:07 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class WxAfterSaleUpdateRequest implements Serializable {

//    @JSONField(name = "out_aftersale_id")
//    private String outAftersaleId;

    @JSONField(name = "aftersale_id")
    private Long aftersaleId;


    /**
     * 金额 分
     */
    @JSONField(name = "orderamt")
    private Long orderamt;

    /**
     * openid
     */
    @JSONField(name = "openid")
    private String openid;

    /**
     * 售后类型，1:退款,2:退款退货 {@link com.soybean.mall.wx.mini.enums.AfterSalesTypeEnum}
     */
    @JSONField(name = "type")
    private Integer type;

    /**
     * 退款原因
     */
    @JSONField(name = "refund_reason")
    private String refundReason;

    /**
     * 退款原因 {@link com.soybean.mall.wx.mini.enums.AfterSalesReasonEnum}
     */
    @JSONField(name = "refund_reason_type")
    private Integer refundReasonType;


}
