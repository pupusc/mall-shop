package com.soybean.mall.service;

import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradePriceVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/4/20 12:44 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CommonService {


    /**
     * 计算商品价格
     *
     * @param tradeItems 多个订单项(商品)
     */
    public TradePriceVO calPrice(List<TradeItemVO> tradeItems) {
        TradePriceVO tradePrice = new TradePriceVO();
        tradePrice.setGoodsPrice(BigDecimal.ZERO);
        tradePrice.setOriginPrice(BigDecimal.ZERO);
        tradePrice.setTotalPrice(BigDecimal.ZERO);
        tradePrice.setBuyPoints(null);
        tradeItems.forEach(t -> {
            BigDecimal buyItemPrice = t.getPrice().multiply(BigDecimal.valueOf(t.getNum()));
            BigDecimal originalPrice = t.getOriginalPrice().multiply(BigDecimal.valueOf(t.getNum()));
            //总价，有定价=定价*数量，否则=原价
            BigDecimal totalPrice = t.getPropPrice() != null ? (new BigDecimal(t.getPropPrice()).multiply(BigDecimal.valueOf(t.getNum()))) : originalPrice;
            // 订单商品总价
            tradePrice.setGoodsPrice(tradePrice.getGoodsPrice().add(buyItemPrice));
            // 订单总金额
            tradePrice.setTotalPrice(tradePrice.getTotalPrice().add(totalPrice));
            // 订单原始总金额
            tradePrice.setOriginPrice(tradePrice.getOriginPrice().add(originalPrice));
            //优惠金额=定价-原价
            tradePrice.setDiscountsPrice(new BigDecimal(0));
            if(totalPrice.compareTo(originalPrice) > 0){
                tradePrice.setDiscountsPrice(totalPrice.subtract(originalPrice));
            }
            //会员优惠
            tradePrice.setVipDiscountPrice(originalPrice.subtract(buyItemPrice));
        });
        return tradePrice;
    }
}
