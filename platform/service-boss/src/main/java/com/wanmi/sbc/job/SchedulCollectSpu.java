package com.wanmi.sbc.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SchedulCollectSpu {     //订单同步

    @Autowired
    private CollectSpuNewJobHandler collectSpuNewJobHandler;      //es_spu_new服务

    //@Scheduled(cron = "0/30 * * * * ?")        //0/30 * * * * ?   表示每5秒 执行任务
    //@Scheduled(cron = "0 34 18  ? * *")          //0 30 23 ? * *   每天23:30触发
    //@Scheduled(cron = "0 00 15 ? * *")        //0 00 15 ? * *   每天15:00触发
    public void doJob(){

        System.out.println("定时任务执行~");

        try {
            collectSpuNewJobHandler.execute(null);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }


}

