package com.soybean.mall.order.response;

import com.wanmi.sbc.marketing.bean.vo.MarkupLevelDetailVO;
import com.wanmi.sbc.order.bean.vo.TradeConfirmItemVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/29 3:16 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class TradeConfirmResp {


    /**
     * 按商家拆分后的订单项
     */
    private List<TradeConfirmItemVO> tradeConfirmItems;

    /**
     * 订单总额
     */
    private BigDecimal totalPrice = BigDecimal.ZERO;

    /**
     * 加价购信息
     */
    private List<MarkupLevelDetailVO> markupLevel;

    /**
     * 是否开团购买(true:开团 false:参团 null:非拼团购买)
     */
    private Boolean openGroupon;

    /**
     * 购买积分，被用于普通订单的积分+金额混合商品
     */
    private Long totalBuyPoint = 0L;

    public BigDecimal getTotalPrice() {
        return tradeConfirmItems.stream().map(i -> i.getTradePrice().getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
