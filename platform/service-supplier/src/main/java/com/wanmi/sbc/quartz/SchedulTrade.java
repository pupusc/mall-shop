package com.wanmi.sbc.quartz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SchedulTrade {     //订单同步

    @Autowired
    private MongdbTradeService mongdbTradeService;      //mongdb服务

    @Autowired
    private MySqlOrderService mySqlOrderService;        //mysql服务

    //@Scheduled(cron = "0/5 * * * * ?")        //0/5 * * * * ?   表示每5秒 执行任务
    @Scheduled(cron = "0 30 23 ? * *")          //0 30 23 ? * *   每天23:30触发
    //@Scheduled(cron = "0 00 15 ? * *")        //0 00 15 ? * *   每天15:00触发

    public void doJob(){

        System.out.println("定时任务执行~");

        String orderTime = mySqlOrderService.getLastTime();

        int pageSize = 1000;

        doData(orderTime,pageSize);

    }

    private void doData(String orderTime, int pageSize) {

        List mongdbList = mongdbTradeService.getList(orderTime,pageSize);

        String next_orderTime = "";

        for (int i=0;i<mongdbList.size();i++){
            Map map = (Map)mongdbList.get(i);
            String id = String.valueOf(map.get("id"));

            next_orderTime = String.valueOf(map.get("time"));

            boolean boo = mySqlOrderService.isExist(id);
            if(boo){
                mySqlOrderService.insert(id);
            }
        }

        if(mongdbList.size() >= pageSize){
            doData(next_orderTime,pageSize);        //接着循环调用
        }

    }

}

