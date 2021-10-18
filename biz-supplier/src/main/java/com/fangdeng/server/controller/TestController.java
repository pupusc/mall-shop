package com.fangdeng.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fangdeng.server.dto.*;
import com.fangdeng.server.job.SyncGoodsJobHandler;
import com.fangdeng.server.job.SyncGoodsPriceJobHandler;
import com.fangdeng.server.job.SyncGoodsStockJobHandler;
import com.fangdeng.server.mapper.GoodsCateSyncMapper;
import com.fangdeng.server.mapper.GoodsLabelMapper;
import com.fangdeng.server.mapper.TagMapper;
import com.fangdeng.server.mq.ProviderTradeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    private SyncGoodsJobHandler job;

    @Autowired
    private SyncGoodsPriceJobHandler priceJob;

    @Autowired
    private ProviderTradeHandler providerTradeHandler;

    @Autowired
    private SyncGoodsStockJobHandler stockJobHandler;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private GoodsCateSyncMapper goodsCateSyncMapper;

    @Autowired
    private GoodsLabelMapper goodsLabelMapper;

    @PostMapping("test")
    public void test(@RequestBody OrderTradeDTO orderTradeDTO){
        BigDecimal a = new BigDecimal(6);
        BigDecimal b = new BigDecimal(6.6);
        BigDecimal c = a.multiply(new BigDecimal(1.1));
        BigDecimal d = a.multiply(new BigDecimal(1.2));
        BigDecimal dd = new BigDecimal("6.6");
        BigDecimal math1 = new BigDecimal(String.valueOf("6")).multiply(new BigDecimal("1.1"));
        BigDecimal math2 = new BigDecimal(String.valueOf("6")).multiply(new BigDecimal("1.2"));
        try {
            //providerTradeHandler.orderPushConsumer(null, JSONObject.toJSONString(orderTradeDTO));
            providerTradeHandler.deliveryStatusSyncConsumer(null,"{\"tid\":\"P202110081721309415007\"}");
        }catch (Exception e){

        }
    }
    @GetMapping("/price")
    public void price(String param){
        try {
            priceJob.execute(param);
        }catch (Exception e){

        }
    }

    @GetMapping("/goods")
    public void goods(String param){
        try {
            job.execute(param);
        }catch (Exception e){

        }
    }

    @GetMapping("/stock")
    public void stock(String param){
        try {
            stockJobHandler.execute(param);
        }catch (Exception e){

        }
    }

    @GetMapping("/status")
    public void status(String param){
        try {
            ProviderTradeDeliveryStatusSyncDTO syncDTO = new ProviderTradeDeliveryStatusSyncDTO();
            syncDTO.setTid(param);
            providerTradeHandler.deliveryStatusSyncConsumer(null, JSON.toJSONString(syncDTO));
        }catch (Exception e){

        }
    }

    @PostMapping("/goods/label/init")
    public void initLabel(@RequestBody List<TagDTO> labels){
        try {
            tagMapper.batchInsert(labels);
            
        }catch (Exception e){
            String a="";
        }
    }

    @PostMapping("/goods/cate/init")
    public void initCate(@RequestBody List<GoodsCateSyncDTO> cates){
        try {

            List<GoodsLabelDTO> labels = goodsLabelMapper.list();
            cates.forEach(c->{
                List<String> names = getName(c,cates);
                List<String> labelIds = labels.stream().filter(p->names.contains(p.getLabelName())).map(p->p.getGoodsLabelId().toString()).collect(Collectors.toList());
                String.join(",",labelIds);
            });
            goodsCateSyncMapper.batchInsert(cates);

        }catch (Exception e){
            String a="";
        }
    }

    private List<String> getName(GoodsCateSyncDTO cate,List<GoodsCateSyncDTO> cates){

        List<GoodsCateSyncDTO> cateSyncDTOS = getParentCates(cate,cates);
       return cateSyncDTOS.stream().map(GoodsCateSyncDTO::getName).collect(Collectors.toList());
    }


    private List<GoodsCateSyncDTO> getParentCates(GoodsCateSyncDTO cate,List<GoodsCateSyncDTO> cates){
        List<GoodsCateSyncDTO> parentCates = new ArrayList<>();
        parentCates.add(cate);
        Optional<GoodsCateSyncDTO> parentCate = cates.stream().filter(p->p.getId().equals(cate.getParentId())).findFirst();
        if(parentCate.isPresent()){
            parentCates.addAll(getParentCates(parentCate.get(),cates));
        }
        return parentCates;
    }

}
