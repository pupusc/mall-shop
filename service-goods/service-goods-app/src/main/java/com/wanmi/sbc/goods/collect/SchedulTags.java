package com.wanmi.sbc.goods.collect;

import com.wanmi.sbc.goods.jpa.JpaManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SchedulTags {     //订单同步

    @Autowired
    BookTags bookTags;
    GoodTags goodTags;

    //@Scheduled(cron = "0/5 * * * * ?")        //0/5 * * * * ?   表示每5秒 执行任务
    //@Scheduled(cron = "0 30 23 ? * *")          //0 30 23 ? * *   每天23:30触发
    //@Scheduled(cron = "0 00 15 ? * *")        //0 00 15 ? * *   每天15:00触发
    public void doJob(){

        System.out.println("定时任务执行~");

        bookTags.doGoods();

    }

}

