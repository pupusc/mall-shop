package com.wanmi.sbc.quartz;

import org.springframework.stereotype.Service;

@Service
public class MySqlOrderService {

    //获取最新的订单时间
    public String getLastTime() {

        String orderTime = "";

        //being



        //end



        if(StringUtil.isBlank(orderTime)){
            String currentTime = StringUtil.getCurrentAllDate();
            orderTime = StringUtil.dayDiff(currentTime,-30);
        }

        return orderTime;

    }

    //判断是否存在
    public boolean isExist(String trade_order_id,String oid) {
        boolean boo = false;
        return boo;
    }

    //同步插入
    public void insert(String trade_order_id,String oid) {
    }
}
