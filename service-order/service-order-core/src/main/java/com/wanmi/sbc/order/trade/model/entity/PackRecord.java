package com.wanmi.sbc.order.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/4/9 1:45 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackRecord implements Serializable {

    /**
     * 打包id
     */
    private String packId;

    /**
     * 打包商品当前的价格
     */
    private BigDecimal packSplitPrice;

    /**
     * 打包的积分
     */
    private Long packPoint;

    /**
     * 打包的知豆
     */
    private Long packKnowLedge;

    /**
     * 数量
     */
    private Integer count;
}
