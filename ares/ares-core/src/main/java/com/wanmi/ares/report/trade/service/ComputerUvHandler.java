package com.wanmi.ares.report.trade.service;

import com.wanmi.ares.report.trade.model.root.TradeBase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2019-10-18 18:24
 */
public class ComputerUvHandler {

    public void computeUv(TradeBase tradeBase, LocalDate date){
        tradeBase.setDate(date);
        tradeBase.setCreateTime(LocalDateTime.now());
        //填充下单转化率 统计时间内，下单人数/访客数UV
        if (tradeBase.getUv() == null || tradeBase.getUv().equals(0L)){
            //填充UV
            tradeBase.setUv(0L);
            tradeBase.setOrderConversion(new BigDecimal("100.00"));
            //填充全店转换率 统计时间内，付款人数/访客数UV
            tradeBase.setAllConversion(new BigDecimal("100.00"));
        }else {
            //填充UV
            tradeBase.setUv(tradeBase.getUv());
            tradeBase.setOrderConversion(new BigDecimal(tradeBase.getOrderUserNum())
                    .multiply(new BigDecimal("100"))
                    .divide(new BigDecimal(tradeBase.getUv()), 2, RoundingMode.HALF_UP));
            //填充全店转换率 统计时间内，付款人数/访客数UV
            tradeBase.setAllConversion(new BigDecimal(tradeBase.getPayUserNum())
                    .multiply(new BigDecimal("100"))
                    .divide(new BigDecimal(tradeBase.getUv()), 2, RoundingMode.HALF_UP));
        }
    }

}
