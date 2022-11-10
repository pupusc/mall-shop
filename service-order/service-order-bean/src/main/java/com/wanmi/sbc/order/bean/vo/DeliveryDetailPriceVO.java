package com.wanmi.sbc.order.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/11/9 7:30 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class DeliveryDetailPriceVO {

    /**
     * 实际运费积分
     */
    private BigDecimal deliveryPointPrice;

    /**
     * 实际运费积分
     */
    private Long deliveryPoint;

    /**
     * 实际运费现金金额
     */
    private BigDecimal deliveryPayPrice;
}
