package com.wanmi.sbc.goods.mini.service.assistant;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantGoodsCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantGoodsUpdateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantSearchRequest;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import com.wanmi.sbc.goods.info.service.GoodsInfoStockService;
import com.wanmi.sbc.goods.info.service.GoodsStockService;
import com.wanmi.sbc.goods.mini.model.goods.WxLiveAssistantGoodsModel;
import com.wanmi.sbc.goods.mini.model.goods.WxLiveAssistantModel;
import com.wanmi.sbc.goods.mini.repository.goods.WxLiveAssistantGoodsRepository;
import com.wanmi.sbc.goods.mini.repository.goods.WxLiveAssistantRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WxLiveAssistantService {

    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private WxLiveAssistantRepository wxLiveAssistantRepository;
    @Autowired
    private WxLiveAssistantGoodsRepository wxLiveAssistantGoodsRepository;
    @Autowired
    private GoodsInfoService goodsInfoService;
    @Autowired
    private GoodsInfoRepository goodsInfoRepository;
    @Autowired
    private GoodsInfoStockService goodsInfoStockService;
    @Autowired
    private GoodsStockService goodsStockService;

    public Long addAssistant(WxLiveAssistantCreateRequest wxLiveAssistantCreateRequest){
        if(wxLiveAssistantCreateRequest.getStartTime() == null || wxLiveAssistantCreateRequest.getEndTime() == null){
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "缺少参数");
        }
        WxLiveAssistantModel wxLiveAssistantModel = WxLiveAssistantModel.create(wxLiveAssistantCreateRequest);
        //发送延时消息
//        sendDelayMessage(wxLiveAssistantModel);
        wxLiveAssistantRepository.save(wxLiveAssistantModel);
        return wxLiveAssistantModel.getId();
    }

//    private void sendDelayMessage(WxLiveAssistantModel wxLiveAssistantModel){
//        Long assistantId = wxLiveAssistantModel.getId();
//
////        LocalDateTime startTime = wxLiveAssistantModel.getStartTime();
////        LocalDateTime now1 = LocalDateTime.now();
////        Duration duration1 = Duration.between(now1, startTime);
////        wxMiniMessageProducer.sendDelay(WxLiveAssistantMessageData.builder().assistantId(assistantId).type(0).time(startTime.format(df)).build(), duration1.toMillis());
//
//        LocalDateTime endTime = wxLiveAssistantModel.getEndTime();
//        LocalDateTime now2 = LocalDateTime.now();
//        Duration duration2 = Duration.between(now2, endTime);
//        wxMiniMessageProducer.sendDelay(WxLiveAssistantMessageData.builder().assistantId(assistantId).type(1).time(endTime.format(df)).build(), duration2.toMillis());
//    }

    @Transactional
    public Map<String, Map<String, Integer>> deleteAssistant(Long id){
        Optional<WxLiveAssistantModel> opt = wxLiveAssistantRepository.findById(id);
        if(!opt.isPresent()) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划不存在");
        WxLiveAssistantModel wxLiveAssistantModel = opt.get();
        wxLiveAssistantModel.setDelFlag(DeleteFlag.YES);
        wxLiveAssistantModel.setUpdateTime(LocalDateTime.now());
        wxLiveAssistantRepository.save(wxLiveAssistantModel);

        log.info("删除直播助手:{}, 所有直播商品将恢复原价和库存", id);
        return resetStockAndProce(id);
    }

    @Transactional
    public Long updateAssistant(WxLiveAssistantCreateRequest wxLiveAssistantCreateRequest){
        Optional<WxLiveAssistantModel> opt = wxLiveAssistantRepository.findById(wxLiveAssistantCreateRequest.getId());
        if(!opt.isPresent()) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播已结束，不能修改");
        LocalDateTime endTime = LocalDateTime.parse(wxLiveAssistantCreateRequest.getEndTime(), df);
        if(endTime.isBefore(LocalDateTime.now())) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "结束时间不能小于当前时间");
        WxLiveAssistantModel wxLiveAssistantModel = opt.get();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(wxLiveAssistantCreateRequest.getTheme() != null){
            wxLiveAssistantModel.setTheme(wxLiveAssistantCreateRequest.getTheme());
        }
        if (wxLiveAssistantCreateRequest.getStartTime() != null){
            wxLiveAssistantModel.setStartTime(LocalDateTime.parse(wxLiveAssistantCreateRequest.getStartTime(), df));
        }
        if(wxLiveAssistantCreateRequest.getEndTime() != null){
            wxLiveAssistantModel.setEndTime(endTime);
        }
        wxLiveAssistantRepository.save(wxLiveAssistantModel);
//        sendDelayMessage(wxLiveAssistantModel);
        return wxLiveAssistantModel.getId();
    }

    public Page<WxLiveAssistantModel> listAssistant(WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest){
        return wxLiveAssistantRepository.findAll(PageRequest.of(wxLiveAssistantSearchRequest.getPageNum(), wxLiveAssistantSearchRequest.getPageSize(),
                Sort.by(Sort.Direction.DESC, "startTime")));
    }

    @Transactional
    public void addGoods(WxLiveAssistantGoodsCreateRequest wxLiveAssistantGoodsCreateRequest){
        Long assistantId = wxLiveAssistantGoodsCreateRequest.getAssistantId();
        if (assistantId == null) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划id不能为空");
        Optional<WxLiveAssistantModel> opt = wxLiveAssistantRepository.findById(assistantId);
        if(!opt.isPresent() || opt.get().getDelFlag().equals(DeleteFlag.YES)) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划不存在");

        WxLiveAssistantModel wxLiveAssistantModel = opt.get();
        if(wxLiveAssistantModel.getEndTime().isBefore(LocalDateTime.now())) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播已结束，不能修改");

        List<WxLiveAssistantGoodsModel> timeConflictGoods = wxLiveAssistantGoodsRepository.findTimeConflictGoods(wxLiveAssistantGoodsCreateRequest.getGoods());
        if(CollectionUtils.isNotEmpty(timeConflictGoods)){
            List<String> timeConflictGoodsIds = timeConflictGoods.stream().map(WxLiveAssistantGoodsModel::getGoodsId).collect(Collectors.toList());
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "以下商品已在直播计划中: " + Arrays.toString(timeConflictGoodsIds.toArray()));
        }
        wxLiveAssistantModel.setGoodsCount(wxLiveAssistantModel.getGoodsCount() + wxLiveAssistantGoodsCreateRequest.getGoods().size());
        wxLiveAssistantRepository.save(wxLiveAssistantModel);

        List<WxLiveAssistantGoodsModel> assistantGoodsModels = WxLiveAssistantGoodsModel.create(wxLiveAssistantGoodsCreateRequest);
        Map<String, List<WxLiveAssistantGoodsModel>> assistantGoodsGroup = assistantGoodsModels.stream().collect(Collectors.groupingBy(WxLiveAssistantGoodsModel::getGoodsId));

        List<String> goods = wxLiveAssistantGoodsCreateRequest.getGoods();
        log.info("以下商品被添加到直播助手{}, 将停止库存同步", Arrays.toString(goods.toArray()));
        List<GoodsInfo> goodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().goodsIds(goods).delFlag(DeleteFlag.NO.toValue()).build());
        Map<String, List<GoodsInfo>> goodsInfoGroup = goodsInfos.stream().collect(Collectors.groupingBy(GoodsInfo::getGoodsId));
        goodsInfoGroup.forEach((k, v) -> {
            Map<String, Integer> olfSyncStockFlag = new HashMap<>();
            WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel = assistantGoodsGroup.get(k).get(0);
            for (GoodsInfo goodsInfo : v) {
                olfSyncStockFlag.put(goodsInfo.getGoodsInfoId(), goodsInfo.getStockSyncFlag());
                goodsInfo.setStockSyncFlag(0);
            }
            wxLiveAssistantGoodsModel.setOlfSyncStockFlag(JSONObject.toJSONString(olfSyncStockFlag));
        });
        wxLiveAssistantGoodsRepository.saveAll(assistantGoodsModels);
        goodsInfoRepository.saveAll(goodsInfos);
    }

    @Transactional
    public Map<String, Map<String, Integer>> deleteGoods(Long id){
        Optional<WxLiveAssistantGoodsModel> opt = wxLiveAssistantGoodsRepository.findById(id);
        if(!opt.isPresent()) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播商品不存在");
        WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel = opt.get();
        wxLiveAssistantGoodsModel.setDelFlag(DeleteFlag.YES);
        wxLiveAssistantGoodsModel.setUpdateTime(LocalDateTime.now());
        wxLiveAssistantGoodsRepository.save(wxLiveAssistantGoodsModel);

        Optional<WxLiveAssistantModel> opt2 = wxLiveAssistantRepository.findById(wxLiveAssistantGoodsModel.getAssistId());
        if(!opt2.isPresent() || opt2.get().getDelFlag().equals(DeleteFlag.YES)){
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划不存在");
        }
        WxLiveAssistantModel wxLiveAssistantModel = opt2.get();
        wxLiveAssistantModel.setGoodsCount(wxLiveAssistantModel.getGoodsCount() - 1);
        wxLiveAssistantRepository.save(wxLiveAssistantModel);

        String goodsId = wxLiveAssistantGoodsModel.getGoodsId();
        log.info("直播商品{}删除，关联的sku将恢复库存同步", goodsId);
        List<GoodsInfo> goodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().goodsIds(Arrays.asList(goodsId)).delFlag(DeleteFlag.NO.toValue()).build());
        List<GoodsInfo> toSave = new ArrayList<>();
        Map<String, Map<String, Integer>> map = resetStockAndProce(wxLiveAssistantGoodsModel, goodsInfos, toSave);
        if (!toSave.isEmpty()) {
            goodsInfoRepository.saveAll(toSave);
        }
        return map;
    }

    public void updateGoodsInfos(WxLiveAssistantGoodsUpdateRequest wxLiveAssistantGoodsUpdateRequest){
        Optional<WxLiveAssistantModel> opt = wxLiveAssistantRepository.findById(wxLiveAssistantGoodsUpdateRequest.getAssistantId());
        if(!opt.isPresent() || opt.get().getDelFlag().equals(DeleteFlag.YES)) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划不存在");
        WxLiveAssistantModel wxLiveAssistantModel = opt.get();
        if(wxLiveAssistantModel.getEndTime().isBefore(LocalDateTime.now())) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播已结束，不能修改");

        Optional<WxLiveAssistantGoodsModel> opt2 = wxLiveAssistantGoodsRepository.findById(wxLiveAssistantGoodsUpdateRequest.getAssistantGoodsId());
        if(!opt2.isPresent() || opt2.get().getDelFlag().equals(DeleteFlag.YES)) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播商品不存在");
        if(!opt2.get().getAssistId().equals(wxLiveAssistantGoodsUpdateRequest.getAssistantId())) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "非法参数");

        List<WxLiveAssistantGoodsUpdateRequest.WxLiveAssistantGoodsInfo> liveAssistantGoodsInfos = wxLiveAssistantGoodsUpdateRequest.getGoodsInfos();
        List<String> goodsInfoIds = liveAssistantGoodsInfos.stream().map(WxLiveAssistantGoodsUpdateRequest.WxLiveAssistantGoodsInfo::getGoodsInfoId).collect(Collectors.toList());
        List<GoodsInfo> goodsInfos = goodsInfoService.findByIds(goodsInfoIds);
        Map<String, List<GoodsInfo>> goodsInfoGroup = goodsInfos.stream().filter(g -> g.getDelFlag().equals(DeleteFlag.NO)).collect(Collectors.groupingBy(GoodsInfo::getGoodsInfoId));
        WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel = opt2.get();
        JSONObject oldGoodsInfo;
        if(wxLiveAssistantGoodsModel.getOldGoodsInfo()!= null){
            oldGoodsInfo = JSONObject.parseObject(wxLiveAssistantGoodsModel.getOldGoodsInfo());
        }else{
            oldGoodsInfo = new JSONObject();
        }
        log.info("以下商品在直播助手中修改价格和库存{}", JSONObject.toJSONString(wxLiveAssistantGoodsUpdateRequest));
        List<GoodsInfo> toUpdateGoodsInfo = new ArrayList<>();
        for (WxLiveAssistantGoodsUpdateRequest.WxLiveAssistantGoodsInfo liveAssistantGoodsInfo : liveAssistantGoodsInfos) {
            String goodsInfoId = liveAssistantGoodsInfo.getGoodsInfoId();
            String price = liveAssistantGoodsInfo.getPrice();
            Integer stock = liveAssistantGoodsInfo.getStock();

            List<GoodsInfo> goodsInfoList = goodsInfoGroup.get(goodsInfoId);
            if(CollectionUtils.isEmpty(goodsInfoList)) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "goodsInfo不存在: " + goodsInfoId);
            GoodsInfo goodsInfo = goodsInfoList.get(0);
            if(!wxLiveAssistantGoodsModel.getGoodsId().equals(goodsInfo.getGoodsId())) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "非法参数");
            HashMap<String, String> map = (HashMap<String, String>) oldGoodsInfo.compute(goodsInfoId, (k, v) -> {
                if (v == null) return new HashMap<>();
                return v;
            });
            boolean updateGoodsInfo = false;
            if(stock != null) {
                updateGoodsInfo = true;
                goodsInfo.setStock(stock.longValue());
                if(!map.containsKey("stock")) map.put("stock", stock.toString());
            }
            if(price != null){
                updateGoodsInfo = true;
                goodsInfo.setMarketPrice(new BigDecimal(price));
                if(!map.containsKey("price")) map.put("price", price);
            }
            if(updateGoodsInfo) toUpdateGoodsInfo.add(goodsInfo);
        }
        if(oldGoodsInfo != null && CollectionUtils.isNotEmpty(liveAssistantGoodsInfos)){
            wxLiveAssistantGoodsModel.setOldGoodsInfo(JSONObject.toJSONString(oldGoodsInfo));
            wxLiveAssistantGoodsRepository.save(wxLiveAssistantGoodsModel);
        }
        if(CollectionUtils.isNotEmpty(toUpdateGoodsInfo)){
            goodsInfoRepository.saveAll(toUpdateGoodsInfo);
        }
    }

    public Page<WxLiveAssistantGoodsModel> listGoods(WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest){
        return wxLiveAssistantGoodsRepository.findAll(wxLiveAssistantGoodsRepository.buildSearchCondition(wxLiveAssistantSearchRequest), PageRequest.of(wxLiveAssistantSearchRequest.getPageNum(), wxLiveAssistantSearchRequest.getPageNum()));
    }

    public WxLiveAssistantModel findAssistantById(Long assistantId){
        Optional<WxLiveAssistantModel> opt = wxLiveAssistantRepository.findById(assistantId);
        if(opt.isPresent()) return opt.get();
        return null;
    }

    @Transactional
    public Map<String, Map<String, Integer>> resetStockAndProce(Long wxLiveAssistantId){
        List<WxLiveAssistantGoodsModel> assistantGoodsModels = wxLiveAssistantGoodsRepository.findAll(wxLiveAssistantGoodsRepository
                .buildSearchCondition(WxLiveAssistantSearchRequest.builder().liveAssistantId(wxLiveAssistantId).build()));
        Map<String, List<WxLiveAssistantGoodsModel>> assistantGoodsGroup = assistantGoodsModels.stream().collect(Collectors.groupingBy(WxLiveAssistantGoodsModel::getGoodsId));
        List<GoodsInfo> goodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().goodsIds(new ArrayList<>(assistantGoodsGroup.keySet())).delFlag(DeleteFlag.NO.toValue()).build());
        Map<String, List<GoodsInfo>> goodsInfoGroup = goodsInfos.stream().collect(Collectors.groupingBy(GoodsInfo::getGoodsId));

        Map<String, Map<String, Integer>> resultMap = new HashMap<>();
        Map<String, Integer> skus = new HashMap<>();
        Map<String, Integer> spus = new HashMap<>();

        List<GoodsInfo> toSave = new ArrayList<>();
        for (List<WxLiveAssistantGoodsModel> value : assistantGoodsGroup.values()) {
            WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel = value.get(0);
            Map<String, Map<String, Integer>> map = resetStockAndProce(wxLiveAssistantGoodsModel, goodsInfoGroup.get(wxLiveAssistantGoodsModel.getGoodsId()), toSave);
            Map<String, Integer> skus1 = map.get("skus");
            Map<String, Integer> spus1 = map.get("spus");
            skus1.forEach((k ,v) -> {
                skus.compute(k, (k2, v2) -> {
                    if(v2 == null){
                        return v;
                    }else {
                        return v + v2;
                    }
                });
            });
            spus1.forEach((k, v) -> {
                spus.compute(k, (k2, v2) -> {
                    if(v2 == null){
                        return v;
                    }else {
                        return v + v2;
                    }
                });
            });
        }
        if(!toSave.isEmpty()){
            goodsInfoRepository.saveAll(toSave);
        }
        resultMap.put("spus", spus);
        resultMap.put("skus", skus);
        return resultMap;
    }

    private Map<String, Map<String, Integer>> resetStockAndProce(WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel, List<GoodsInfo> goodsInfos, List<GoodsInfo> toSave){
        String olfSyncStockFlagStr = wxLiveAssistantGoodsModel.getOlfSyncStockFlag();
        JSONObject olfSyncStockFlag = JSONObject.parseObject(olfSyncStockFlagStr);
        String oldGoodsInfoStr = wxLiveAssistantGoodsModel.getOldGoodsInfo();
        Map oldGoodsInfo = JSONObject.parseObject(oldGoodsInfoStr, Map.class);

        Map<String, Map<String, Integer>> resultMap = new HashMap<>();
        Map<String, Integer> skus = new HashMap<>();
        Map<String, Integer> spus = new HashMap<>();

        for (GoodsInfo goodsInfo : goodsInfos) {
            Integer oldFlag = (Integer) olfSyncStockFlag.get(goodsInfo);
            if(oldFlag == null || oldFlag == 0){
                //开播之前未开同步

            }else {
                //开播之前同步了,同步一次库存
                goodsInfo.setStockSyncFlag(1);
                Map<String, Map<String, Integer>> map = goodsStockService.partialUpdateStock(goodsInfo.getErpGoodsInfoNo(), "", "1", "10");
                Map<String, Integer> skus1 = map.get("skus");
                Map<String, Integer> spus1 = map.get("spus");
                skus1.forEach((k ,v) -> {
                    skus.compute(k, (k2, v2) -> {
                        if(v2 == null){
                            return v;
                        }else {
                            return v + v2;
                        }
                    });
                });
                spus1.forEach((k, v) -> {
                    spus.compute(k, (k2, v2) -> {
                        if(v2 == null){
                            return v;
                        }else {
                            return v + v2;
                        }
                    });
                });
            }
            Map<String, String> oldInfo = (Map<String, String>) oldGoodsInfo.get(goodsInfo);
            if(oldInfo != null){
                String price = oldInfo.get("price");
//                String stock = oldInfo.get("stock");
                if(price != null){
                    goodsInfo.setMarketPrice(new BigDecimal(price));
                    toSave.add(goodsInfo);
                }
            }
        }
        resultMap.put("spus", spus);
        resultMap.put("skus", skus);
        return resultMap;
    }

}
