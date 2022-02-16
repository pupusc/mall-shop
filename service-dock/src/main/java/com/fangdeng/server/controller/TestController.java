package com.fangdeng.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fangdeng.server.dto.*;
import com.fangdeng.server.entity.GoodsCateSync;
import com.fangdeng.server.entity.Tag;
import com.fangdeng.server.job.SyncGoodsJobHandler;
import com.fangdeng.server.job.SyncGoodsPriceJobHandler;
import com.fangdeng.server.job.SyncGoodsStockJobHandler;
import com.fangdeng.server.mapper.GoodsCateSyncMapper;
import com.fangdeng.server.mapper.TagMapper;
import com.fangdeng.server.mq.ProviderTradeHandler;
import com.fangdeng.server.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/test")
@Slf4j
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
    private RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    private GoodsService goodsService;


    @PostMapping("test")
    public void test(@RequestBody OrderTradeDTO orderTradeDTO){
            List<String> keys =new ArrayList<>();
            keys.add("GOODS_INFO_STOCK_2c90e856787e5162017881b8cf8d003c");
            keys.add("GOODS_INFO_STOCK_2c90e856787e5162017881b8cf8d0031");
            Map<String,String> map = new HashMap<>();
            try {
                redisTemplate.setKeySerializer(new StringRedisSerializer());
                redisTemplate.setValueSerializer(new StringRedisSerializer());
                List<Object> objects = redisTemplate.executePipelined((RedisCallback<Object>) redisConnection -> {
                    for (String  key : keys) {
                        redisConnection.get(redisTemplate.getStringSerializer().serialize(key));
                    }
                    return null;
                });
                String a="";
            } catch (Exception e) {

            }



        try {
            providerTradeHandler.orderPushConsumer(null, JSONObject.toJSONString(orderTradeDTO));
            //providerTradeHandler.deliveryStatusSyncConsumer(null,"{\"tid\":\"P202110081721309415007\"}");
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
    public void initLabel(@RequestBody List<String> labels){
        log.info("init label start");
        try {
            List<String> oldTags = tagMapper.listTagName();
            List<String> tags = labels.stream().filter(p-> !(p.contains("其他") || p.contains("其它"))).distinct().collect(Collectors.toList());
            List<String> tagList = tags.stream().filter(p->!oldTags.contains(p)).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(tagList)){
              tagMapper.batchInsert(tagList);
            }
        }catch (Exception e){
            log.warn("init label error",e);
        }
    }

    @PostMapping("/goods/cate/init")
    public void initCate(@RequestBody List<GoodsCateSync> cates){
        try {

            List<Tag> tagDTOS = tagMapper.list();
            cates.forEach(c->{
                List<String> names = getName(c,cates);
                List<Tag> tags = tagDTOS.stream().filter(p->names.contains(p.getTagName())).collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Tag:: getTagName))), ArrayList::new));
                List<String> labelIds = tags.stream().map(p->p.getId().toString()).collect(Collectors.toList());
                c.setLabelIds(String.join(",",labelIds));
            });
            goodsCateSyncMapper.batchInsert(cates);

        }catch (Exception e){
            log.warn("init cate error",e);
        }
    }

    private List<String> getName(GoodsCateSync cate, List<GoodsCateSync> cates){

        List<GoodsCateSync> cateSyncDTOS = getParentCates(cate,cates);
       return cateSyncDTOS.stream().map(GoodsCateSync::getName).collect(Collectors.toList());
    }


    private List<GoodsCateSync> getParentCates(GoodsCateSync cate, List<GoodsCateSync> cates){
        List<GoodsCateSync> parentCates = new ArrayList<>();
        parentCates.add(cate);
        Optional<GoodsCateSync> parentCate = cates.stream().filter(p->p.getId().equals(cate.getParentId())).findFirst();
        if(parentCate.isPresent()){
            parentCates.addAll(getParentCates(parentCate.get(),cates));
        }
        return parentCates;
    }

    @GetMapping("/goods/audit")
    public void audit(@RequestParam("goodsNo")String goodsNo){
        goodsService.auditGoods(goodsNo);
    }


    @GetMapping("/delete/rediskey")
    public void deleteRedisKey(@RequestParam("key")String key) {
        redisTemplate.delete(key);
    }


}
