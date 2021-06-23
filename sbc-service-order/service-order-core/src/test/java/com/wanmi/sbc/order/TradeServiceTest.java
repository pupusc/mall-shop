package com.wanmi.sbc.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.order.returnorder.service.ReturnOrderService;
import com.wanmi.sbc.order.settlement.SettlementAnalyseService;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.service.TradeService;
import com.wanmi.sbc.order.trade.service.TradeSettingService;
import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

public class TradeServiceTest extends BaseTest {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private TradeSettingService tradeSettingService;

    @Autowired
    private ReturnOrderService returnOrderService;

    @Autowired
    private SettlementAnalyseService settlementAnalyseService;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Test
    public void testQueryTradeByDate() {
        LocalDateTime end = LocalDateTime.now();
    }

    @Test
    public void queryTradeConfigsTest() {
        List<ConfigVO> list = tradeSettingService.queryTradeConfigs();
        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public void queryReturnOrderByDateTest() {
//        LocalDateTime end = LocalDateTime.now();
//        Page<ReturnOrder> returnOrders = returnOrderService.queryReturnOrderByDate(end, ReturnFlowState.RECEIVED);
//        Assert.assertTrue(returnOrders.getTotalElements() > 0);
    }

//    @Test
//    public void queryReturnOrderByPageTest() {
//        LocalDateTime end = LocalDateTime.now();
//        List<ReturnOrder> returnOrders = returnOrderService.queryReturnOrderByPage(end, ReturnFlowState.RECEIVED, new
//                PageRequest(0, 1000));
//        Assert.assertTrue(returnOrders.size() > 0);
//    }

    @Test
    public void testUpdate() {
        Query query = new Query();
        query.addCriteria(Criteria.where("tradeState.createTime").gte(DateUtil.parse("2017-12-05 23:00:00", DateUtil
                .FMT_TIME_1)));
        System.out.println(query.toString());
        List<Trade> trades = mongoTemplate.find(query, Trade.class, "trade");
        trades.forEach(t -> System.out.println(t.getEncloses()));
        System.out.println("-------------------------------------------------------------------");
        Update update = new Update();
        update.set("encloses", "test");
        mongoTemplate.updateMulti(query, update, Trade.class);
        trades = mongoTemplate.find(query, Trade.class, "trade");
        trades.forEach(t -> System.out.println(t.getEncloses()));
    }

    @Test
    public void testSettlement() {
        Trade trade = tradeService.detail("O201810261430102239");
        Calendar calendar = Calendar.getInstance();

//        trade.getTradeState().setEndTime(LocalDateTime.of(2018, Month.OCTOBER, 26, 14, 31));
//        tradeService.saveTrade(trade);
//        calendar.set(2018, 10 - 1, 27);
//        settlementAnalyseService.analyseSettlement(calendar.getTime());

//        trade.getTradeState().setEndTime(LocalDateTime.of(2018, Month.OCTOBER, 27, 16, 58));
//        tradeService.saveTrade(trade);
//        calendar.set(2018, 10 - 1, 28);
//        settlementAnalyseService.analyseSettlement(calendar.getTime());

//        calendar.set(2018, 10 - 1, 29);
//        settlementAnalyseService.analyseSettlement(calendar.getTime());

        calendar.set(2018, 10 - 1, 30);
    }


    @Test
    public void queryReturnOrderByEndDateTest() {
//        LocalDateTime endTime = LocalDateTime.now().plusDays(5);
//        int count = returnOrderService.countReturnOrderByEndDate(endTime);
//        Assert.assertEquals(count, 1);
    }

    @Test
    public void testJSONParse() {
        String date = "{\"day\":\"1\"}";
        JSONObject date1 = JSON.parseObject(date);
        int day = Integer.valueOf(date1.get("day").toString());
        System.out.println("day--->" + day);
    }

}
