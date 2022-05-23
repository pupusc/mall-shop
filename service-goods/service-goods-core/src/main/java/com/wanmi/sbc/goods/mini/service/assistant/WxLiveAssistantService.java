package com.wanmi.sbc.goods.mini.service.assistant;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.enums.HasAssistantGoodsValidEnum;
import com.wanmi.sbc.goods.api.request.goodsstock.GuanYiSyncGoodsStockRequest;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantGoodsCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantGoodsUpdateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantSearchRequest;
import com.wanmi.sbc.goods.bean.wx.vo.assistant.WxLiveAssistantGoodsConfigVo;
import com.wanmi.sbc.goods.bean.wx.vo.assistant.WxLiveAssistantGoodsInfoConfigVo;
import com.wanmi.sbc.goods.bean.wx.vo.assistant.WxLiveAssistantGoodsUpdateVo;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import com.wanmi.sbc.goods.info.service.GoodsService;
import com.wanmi.sbc.goods.info.service.GoodsStockService;
import com.wanmi.sbc.goods.mini.model.goods.WxLiveAssistantGoodsModel;
import com.wanmi.sbc.goods.mini.model.goods.WxLiveAssistantModel;
import com.wanmi.sbc.goods.mini.repository.goods.WxLiveAssistantGoodsRepository;
import com.wanmi.sbc.goods.mini.repository.goods.WxLiveAssistantRepository;
import com.wanmi.sbc.goods.mini.service.goods.WxGoodsService;
import com.wanmi.sbc.goods.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
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
    private GoodsService goodsService;
    @Autowired
    private GoodsInfoRepository goodsInfoRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsStockService goodsStockService;
    @Autowired
    private WxGoodsService wxGoodsService;
    @Autowired
    private RedisService redisService;

    @Value("${default.providerId}")
    private String defaultProviderId;

    @Transactional
    public Long addAssistant(WxLiveAssistantCreateRequest wxLiveAssistantCreateRequest){
        if(wxLiveAssistantCreateRequest.getStartTime() == null || wxLiveAssistantCreateRequest.getEndTime() == null){
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "缺少参数");
        }
        WxLiveAssistantModel wxLiveAssistantModel = WxLiveAssistantModel.create(wxLiveAssistantCreateRequest);
        wxLiveAssistantRepository.save(wxLiveAssistantModel);

        if(CollectionUtils.isNotEmpty(wxLiveAssistantCreateRequest.getGoodsId())){
            WxLiveAssistantGoodsCreateRequest wxLiveAssistantGoodsCreateRequest = new WxLiveAssistantGoodsCreateRequest();
            wxLiveAssistantGoodsCreateRequest.setAssistantId(wxLiveAssistantModel.getId());
            wxLiveAssistantGoodsCreateRequest.setGoods(wxLiveAssistantCreateRequest.getGoodsId());
            addGoods(wxLiveAssistantGoodsCreateRequest, wxLiveAssistantModel);
        }

        return wxLiveAssistantModel.getId();
    }

    @Transactional
    public void deleteAssistant(Long id){
        Optional<WxLiveAssistantModel> opt = wxLiveAssistantRepository.findById(id);
        if(!opt.isPresent() || opt.get().getDelFlag().equals(DeleteFlag.YES)) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划不存在");
        WxLiveAssistantModel wxLiveAssistantModel = opt.get();
        if(wxLiveAssistantModel.getEndTime().isBefore(LocalDateTime.now())) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播已结束，不能删除");
        wxLiveAssistantModel.setDelFlag(DeleteFlag.YES);
        wxLiveAssistantModel.setUpdateTime(LocalDateTime.now());
        wxLiveAssistantRepository.save(wxLiveAssistantModel);

//        log.info("删除直播助手:{}, 所有直播商品将恢复原价和库存", id);
//        return resetStockAndProce(id);
    }

    @Transactional
    public void updateAssistant(WxLiveAssistantCreateRequest wxLiveAssistantCreateRequest){
        Optional<WxLiveAssistantModel> opt = wxLiveAssistantRepository.findById(wxLiveAssistantCreateRequest.getId());
        if(!opt.isPresent() || opt.get().getDelFlag().equals(DeleteFlag.YES)) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划不存在");
        WxLiveAssistantModel wxLiveAssistantModel = opt.get();
//        if(wxLiveAssistantModel.getEndTime().isBefore(LocalDateTime.now())) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播已结束，不能修改");
        if(StringUtils.isNotBlank(wxLiveAssistantCreateRequest.getTheme())){
            wxLiveAssistantModel.setTheme(wxLiveAssistantCreateRequest.getTheme());
        }


        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = LocalDateTime.parse(wxLiveAssistantCreateRequest.getStartTime(), df);
        LocalDateTime endTime = LocalDateTime.parse(wxLiveAssistantCreateRequest.getEndTime(), df);
        if(endTime.isBefore(startTime)) throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "开始时间不能大于结束时间");
        if(endTime.isBefore(now)) throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "结束时间不能小于现在");
//        Map<String, String> resultMap = new HashMap<>();
        if (wxLiveAssistantCreateRequest.getStartTime() != null){
            if(!wxLiveAssistantModel.getStartTime().isEqual(startTime)){
                wxLiveAssistantModel.setStartTime(startTime);
            }
        }
        if(wxLiveAssistantCreateRequest.getEndTime() != null){
            if(!wxLiveAssistantModel.getEndTime().isEqual(endTime)){
                wxLiveAssistantModel.setEndTime(endTime);
//                resultMap.put("endTime", wxLiveAssistantCreateRequest.getEndTime());
            }
        }
        wxLiveAssistantRepository.save(wxLiveAssistantModel);
//        resultMap.put("id", wxLiveAssistantModel.getId().toString());
//        return resultMap;
    }

    public Page<WxLiveAssistantModel> listAssistant(WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest){
        return wxLiveAssistantRepository.findAll(wxLiveAssistantRepository.buildSearchCondition(wxLiveAssistantSearchRequest), PageRequest.of(wxLiveAssistantSearchRequest.getPageNum(), wxLiveAssistantSearchRequest.getPageSize(),
                Sort.by(Sort.Direction.DESC, "startTime")));
    }

    @Transactional
    public List<String> addGoods(WxLiveAssistantGoodsCreateRequest wxLiveAssistantGoodsCreateRequest){
        Long assistantId = wxLiveAssistantGoodsCreateRequest.getAssistantId();
        if (assistantId == null) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划id不能为空");
        Optional<WxLiveAssistantModel> opt = wxLiveAssistantRepository.findById(assistantId);
        if(!opt.isPresent() || opt.get().getDelFlag().equals(DeleteFlag.YES)) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划不存在");
        if(CollectionUtils.isEmpty(wxLiveAssistantGoodsCreateRequest.getGoods())) return new ArrayList<>();

        WxLiveAssistantModel wxLiveAssistantModel = opt.get();
        return addGoods(wxLiveAssistantGoodsCreateRequest, wxLiveAssistantModel);
    }


    @Transactional
    public List<String> addGoods(WxLiveAssistantGoodsCreateRequest wxLiveAssistantGoodsCreateRequest, WxLiveAssistantModel wxLiveAssistantModel){

        if(wxLiveAssistantModel.getEndTime().isBefore(LocalDateTime.now())) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播已结束，不能修改");

        List<WxLiveAssistantGoodsModel> timeConflictGoods = wxLiveAssistantGoodsRepository.listAssistantGoodsByGoodsIds(wxLiveAssistantGoodsCreateRequest.getGoods());
        if(CollectionUtils.isNotEmpty(timeConflictGoods)){
            List<String> timeConflictGoodsIds = timeConflictGoods.stream().map(WxLiveAssistantGoodsModel::getGoodsId).collect(Collectors.toList());
            List<Goods> goodsList = goodsRepository.findAllByGoodsIdIn(timeConflictGoodsIds);
            List<String> goodsNoList = goodsList.stream().map(Goods::getGoodsNo).collect(Collectors.toList());
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, Arrays.toString(goodsNoList.toArray()) + " 已添加至其他直播计划，无法重复添加");
        }
        wxLiveAssistantModel.setGoodsCount(wxLiveAssistantModel.getGoodsCount() + wxLiveAssistantGoodsCreateRequest.getGoods().size());
        wxLiveAssistantRepository.save(wxLiveAssistantModel);


        List<GoodsInfo> goodsInfoList = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().goodsIds(wxLiveAssistantGoodsCreateRequest.getGoods()).delFlag(DeleteFlag.NO.toValue()).build());
        if (CollectionUtils.isEmpty(goodsInfoList)) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划商品为空，无法添加进去");
        }

        /**
         * 最新更新数据
         */
        Map<String, List<WxLiveAssistantGoodsInfoConfigVo>> goodsInfoId2AssistantGoodsInfoConfigMap = new HashMap<>();

        //直播计划商品库存同步
        Map<String, Map<String, Integer>> goodsId2GoodsInfoId2StockFlagSyncMap = new HashMap<>(goodsInfoList.size());
        //直播计划商品价格
        Map<String, Map<String, BigDecimal>> goodsId2GoodsInfoId2PriceMap = new HashMap<>(goodsInfoList.size());

        for (GoodsInfo goodsInfo : goodsInfoList) {
            List<WxLiveAssistantGoodsInfoConfigVo> assistantGoodsInfoConfigVoList = goodsInfoId2AssistantGoodsInfoConfigMap.computeIfAbsent(goodsInfo.getGoodsId(), k -> new ArrayList<>());
            WxLiveAssistantGoodsInfoConfigVo wxLiveAssistantGoodsInfoConfigVo = new WxLiveAssistantGoodsInfoConfigVo();
            wxLiveAssistantGoodsInfoConfigVo.setGoodsInfoId(goodsInfo.getGoodsInfoId());
            wxLiveAssistantGoodsInfoConfigVo.setStock(goodsInfo.getStock());
            wxLiveAssistantGoodsInfoConfigVo.setWxPrice(goodsInfo.getMarketPrice());
            assistantGoodsInfoConfigVoList.add(wxLiveAssistantGoodsInfoConfigVo);

            Map<String, Integer> goodsInfo2StockFlagSyncMap = goodsId2GoodsInfoId2StockFlagSyncMap.get(goodsInfo.getGoodsId());
            if (goodsInfo2StockFlagSyncMap == null) {
                goodsInfo2StockFlagSyncMap = new HashMap<>();
            }
            goodsInfo2StockFlagSyncMap.put(goodsInfo.getGoodsInfoId(), goodsInfo.getStockSyncFlag());
            goodsId2GoodsInfoId2StockFlagSyncMap.put(goodsInfo.getGoodsId(), goodsInfo2StockFlagSyncMap);

            Map<String, BigDecimal> goodsInfo2PriceMap = goodsId2GoodsInfoId2PriceMap.get(goodsInfo.getGoodsId());
            if (goodsInfo2PriceMap == null) {
                goodsInfo2PriceMap = new HashMap<>();
            }
            goodsInfo2PriceMap.put(goodsInfo.getGoodsInfoId(), goodsInfo.getMarketPrice());
            goodsId2GoodsInfoId2PriceMap.put(goodsInfo.getGoodsInfoId(), goodsInfo2PriceMap);

        }

        //新增
        List<WxLiveAssistantGoodsModel> assistantGoodsModelList = new ArrayList<>();
        for (String goodsId : wxLiveAssistantGoodsCreateRequest.getGoods()) {
            WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel = new WxLiveAssistantGoodsModel();
            wxLiveAssistantGoodsModel.setAssistId(wxLiveAssistantGoodsCreateRequest.getAssistantId());
            wxLiveAssistantGoodsModel.setGoodsId(goodsId);
            List<WxLiveAssistantGoodsInfoConfigVo> assistantGoodsInfoConfigVoList = goodsInfoId2AssistantGoodsInfoConfigMap.get(goodsId);
            if (!CollectionUtils.isEmpty(assistantGoodsInfoConfigVoList)) {
                wxLiveAssistantGoodsModel.setNewGoodsInfoJson(JSON.toJSONString(assistantGoodsInfoConfigVoList));
            }
            if (goodsId2GoodsInfoId2StockFlagSyncMap.get(goodsId) != null) {
                wxLiveAssistantGoodsModel.setOlfSyncStockFlag(JSON.toJSONString(goodsId2GoodsInfoId2StockFlagSyncMap));
            }
            if (goodsId2GoodsInfoId2PriceMap.get(goodsId) != null) {
                wxLiveAssistantGoodsModel.setOldGoodsInfo(JSON.toJSONString(goodsId2GoodsInfoId2PriceMap));
            }
            LocalDateTime now = LocalDateTime.now();
            wxLiveAssistantGoodsModel.setCreateTime(now);
            wxLiveAssistantGoodsModel.setUpdateTime(now);
            wxLiveAssistantGoodsModel.setDelFlag(DeleteFlag.NO);
            assistantGoodsModelList.add(wxLiveAssistantGoodsModel);
        }

        //同步库存信息
        boolean isSync = false;
        if (wxLiveAssistantModel.getHasAssistantGoodsValid() != null && wxLiveAssistantModel.getHasAssistantGoodsValid() == HasAssistantGoodsValidEnum.SYNC.getCode()) {
            this.lockGoodsAndGoodsInfo(assistantGoodsModelList);
            isSync = true;
        }

        wxLiveAssistantGoodsRepository.saveAll(assistantGoodsModelList);
        return isSync ? wxLiveAssistantGoodsCreateRequest.getGoods() : new ArrayList<>();
//
//        List<String> goodsIdList = assistantGoodsModels.stream().map(WxLiveAssistantGoodsModel::getGoodsId).collect(Collectors.toList());
//        List<GoodsInfo> goodsInfoList = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().goodsIds(goodsIdList).delFlag(DeleteFlag.NO.toValue()).build());
//        for (GoodsInfo goodsInfo : goodsInfoList) {
//
//        }
//        Map<String, List<WxLiveAssistantGoodsModel>> assistantGoodsGroup = assistantGoodsModels.stream().collect(Collectors.groupingBy(WxLiveAssistantGoodsModel::getGoodsId));
//
//        List<String> goodIdList = wxLiveAssistantGoodsCreateRequest.getGoods();
//        log.info("以下商品被添加到直播助手{}, 将停止库存同步", Arrays.toString(goodIdList.toArray()));
//        List<GoodsInfo> goodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().goodsIds(goodIdList).delFlag(DeleteFlag.NO.toValue()).build());
//        Map<String, List<GoodsInfo>> goodsInfoGroup = goodsInfos.stream().collect(Collectors.groupingBy(GoodsInfo::getGoodsId));
//        goodsInfoGroup.forEach((k, v) -> {
//            Map<String, Integer> olfSyncStockFlag = new HashMap<>();
//            WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel = assistantGoodsGroup.get(k).get(0);
//            for (GoodsInfo goodsInfo : v) {
//                olfSyncStockFlag.put(goodsInfo.getGoodsInfoId(), goodsInfo.getStockSyncFlag());
//                goodsInfo.setStockSyncFlag(0);
//            }
//            wxLiveAssistantGoodsModel.setOlfSyncStockFlag(JSONObject.toJSONString(olfSyncStockFlag));
//        });
//
//        goodsInfoRepository.saveAll(goodsInfos);
    }

    @Transactional
    public List<String> deleteGoods(Long id){
        Optional<WxLiveAssistantGoodsModel> opt = wxLiveAssistantGoodsRepository.findById(id);
        if(!opt.isPresent() || opt.get().getDelFlag().equals(DeleteFlag.YES)) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播商品不存在");
        WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel = opt.get();

        Optional<WxLiveAssistantModel> opt2 = wxLiveAssistantRepository.findById(wxLiveAssistantGoodsModel.getAssistId());
        if(!opt2.isPresent() || opt2.get().getDelFlag().equals(DeleteFlag.YES)){
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划不存在");
        }
        WxLiveAssistantModel wxLiveAssistantModel = opt2.get();
        if(wxLiveAssistantModel.getEndTime().isBefore(LocalDateTime.now())) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播已结束，不能删除");
        }
        wxLiveAssistantModel.setGoodsCount(wxLiveAssistantModel.getGoodsCount() - 1);
        wxLiveAssistantRepository.save(wxLiveAssistantModel);

        boolean isSync = false;
        if (wxLiveAssistantModel.getHasAssistantGoodsValid() != null && wxLiveAssistantModel.getHasAssistantGoodsValid() == HasAssistantGoodsValidEnum.SYNC.getCode()) {
            this.unlockGoodsAndGoodsInfo(Collections.singletonList(wxLiveAssistantGoodsModel));
            isSync = true;
        }
        wxLiveAssistantGoodsModel.setDelFlag(DeleteFlag.YES);
        wxLiveAssistantGoodsModel.setUpdateTime(LocalDateTime.now());

        wxLiveAssistantGoodsRepository.save(wxLiveAssistantGoodsModel);
        return isSync ? Collections.singletonList(wxLiveAssistantGoodsModel.getGoodsId()) : new ArrayList<>();
    }


    @Transactional
    public String updateGoodsInfos(WxLiveAssistantGoodsUpdateRequest wxLiveAssistantGoodsUpdateRequest){
        Optional<WxLiveAssistantModel> opt = wxLiveAssistantRepository.findById(wxLiveAssistantGoodsUpdateRequest.getAssistantId());
        if(!opt.isPresent() || opt.get().getDelFlag().equals(DeleteFlag.YES)) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划不存在");
        }
        WxLiveAssistantModel wxLiveAssistantModel = opt.get();
        if(wxLiveAssistantModel.getEndTime().isBefore(LocalDateTime.now())) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播已结束，不能修改");
        }


        Optional<WxLiveAssistantGoodsModel> opt2 = wxLiveAssistantGoodsRepository.findById(wxLiveAssistantGoodsUpdateRequest.getAssistantGoodsId());
        if(!opt2.isPresent() || opt2.get().getDelFlag().equals(DeleteFlag.YES)) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播商品不存在");
        }
        if(!opt2.get().getAssistId().equals(wxLiveAssistantGoodsUpdateRequest.getAssistantId())) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划不存在");
        }
        WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel = opt2.get();

        List<WxLiveAssistantGoodsUpdateVo> wxLiveAssistantGoodsUpdateVoList = new ArrayList<>();

        List<WxLiveAssistantGoodsInfoConfigVo> assistantGoodsInfoConfigVoList = new ArrayList<>();
        for (WxLiveAssistantGoodsUpdateRequest.WxLiveAssistantGoodsInfo assistantGoodsInfoParam : wxLiveAssistantGoodsUpdateRequest.getGoodsInfos()) {
            if (StringUtils.isBlank(assistantGoodsInfoParam.getPrice()) || assistantGoodsInfoParam.getStock() == null || assistantGoodsInfoParam.getStock() <= 0) {
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, assistantGoodsInfoParam.getGoodsInfoId() + "对应的商品库存不能为空");
            }
            WxLiveAssistantGoodsInfoConfigVo wxLiveAssistantGoodsInfoConfigVo = new WxLiveAssistantGoodsInfoConfigVo();
            wxLiveAssistantGoodsInfoConfigVo.setGoodsInfoId(assistantGoodsInfoParam.getGoodsInfoId());
            wxLiveAssistantGoodsInfoConfigVo.setStock(assistantGoodsInfoParam.getStock().longValue());
            wxLiveAssistantGoodsInfoConfigVo.setWxPrice(new BigDecimal(assistantGoodsInfoParam.getPrice()));
            assistantGoodsInfoConfigVoList.add(wxLiveAssistantGoodsInfoConfigVo);

            WxLiveAssistantGoodsUpdateVo wxLiveAssistantGoodsUpdateVo = new WxLiveAssistantGoodsUpdateVo();
            wxLiveAssistantGoodsUpdateVo.setGoodsInfoId(assistantGoodsInfoParam.getGoodsInfoId());
            wxLiveAssistantGoodsUpdateVo.setWxPrice(new BigDecimal(assistantGoodsInfoParam.getPrice()));
            wxLiveAssistantGoodsUpdateVo.setWxStock(assistantGoodsInfoParam.getStock().longValue());
            wxLiveAssistantGoodsUpdateVoList.add(wxLiveAssistantGoodsUpdateVo);
        }

        wxLiveAssistantGoodsModel.setNewGoodsInfoJson(JSON.toJSONString(assistantGoodsInfoConfigVoList));

        if (wxLiveAssistantModel.getHasAssistantGoodsValid() != null && wxLiveAssistantModel.getHasAssistantGoodsValid() == HasAssistantGoodsValidEnum.SYNC.getCode()) {
            this.lockGoodsAndGoodsInfo(Collections.singletonList(wxLiveAssistantGoodsModel));
        }

        wxGoodsService.toAudit(wxLiveAssistantGoodsModel.getGoodsId(), wxLiveAssistantGoodsUpdateVoList);

        wxLiveAssistantGoodsRepository.save(wxLiveAssistantGoodsModel);

        return wxLiveAssistantGoodsModel.getGoodsId();
    }

//    @Transactional
//    public String updateGoodsInfos(WxLiveAssistantGoodsUpdateRequest wxLiveAssistantGoodsUpdateRequest){
//        Optional<WxLiveAssistantModel> opt = wxLiveAssistantRepository.findById(wxLiveAssistantGoodsUpdateRequest.getAssistantId());
//        if(!opt.isPresent() || opt.get().getDelFlag().equals(DeleteFlag.YES)) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划不存在");
//        WxLiveAssistantModel wxLiveAssistantModel = opt.get();
//        if(wxLiveAssistantModel.getEndTime().isBefore(LocalDateTime.now())) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播已结束，不能修改");
//
//        Optional<WxLiveAssistantGoodsModel> opt2 = wxLiveAssistantGoodsRepository.findById(wxLiveAssistantGoodsUpdateRequest.getAssistantGoodsId());
//        if(!opt2.isPresent() || opt2.get().getDelFlag().equals(DeleteFlag.YES)) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播商品不存在");
//        if(!opt2.get().getAssistId().equals(wxLiveAssistantGoodsUpdateRequest.getAssistantId())) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划不存在");
//
//        List<WxLiveAssistantGoodsUpdateRequest.WxLiveAssistantGoodsInfo> liveAssistantGoodsInfos = wxLiveAssistantGoodsUpdateRequest.getGoodsInfos();
////        List<String> goodsInfoIds = liveAssistantGoodsInfos.stream().map(WxLiveAssistantGoodsUpdateRequest.WxLiveAssistantGoodsInfo::getGoodsInfoId).collect(Collectors.toList());
////        List<GoodsInfo> goodsInfos = goodsInfoService.findByIds(goodsInfoIds);
////        Map<String, List<GoodsInfo>> goodsInfoGroup = goodsInfos.stream().filter(g -> g.getDelFlag().equals(DeleteFlag.NO)).collect(Collectors.groupingBy(GoodsInfo::getGoodsInfoId));
//        WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel = opt2.get();
//        String goodsId = wxLiveAssistantGoodsModel.getGoodsId();
//        Goods goods = goodsService.findByGoodsId(goodsId);
//
//        List<GoodsInfo> goodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().goodsIds(Collections.singletonList(goodsId)).delFlag(DeleteFlag.NO.toValue()).build());
//        Map<String, List<GoodsInfo>> goodsInfoGroup = goodsInfos.stream().filter(g -> g.getDelFlag().equals(DeleteFlag.NO)).collect(Collectors.groupingBy(GoodsInfo::getGoodsInfoId));
//
//        JSONObject oldGoodsInfo;
//        if(wxLiveAssistantGoodsModel.getOldGoodsInfo()!= null){
//            oldGoodsInfo = JSONObject.parseObject(wxLiveAssistantGoodsModel.getOldGoodsInfo());
//        }else{
//            oldGoodsInfo = new JSONObject();
//        }
//        log.info("以下商品在直播助手中修改价格和库存{}", JSONObject.toJSONString(wxLiveAssistantGoodsUpdateRequest));
//        List<GoodsInfo> toUpdateGoodsInfo = new ArrayList<>();
//        for (WxLiveAssistantGoodsUpdateRequest.WxLiveAssistantGoodsInfo liveAssistantGoodsInfo : liveAssistantGoodsInfos) {
//            String goodsInfoId = liveAssistantGoodsInfo.getGoodsInfoId();
//            String price = liveAssistantGoodsInfo.getPrice();
//            Integer stock = liveAssistantGoodsInfo.getStock();
//
//            List<GoodsInfo> goodsInfoList = goodsInfoGroup.get(goodsInfoId);
//            if(CollectionUtils.isEmpty(goodsInfoList)) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "goodsInfo不存在: " + goodsInfoId);
//            GoodsInfo goodsInfo = goodsInfoList.get(0);
//            if(!goodsId.equals(goodsInfo.getGoodsId())) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "非法参数");
//            JSONObject map = (JSONObject) oldGoodsInfo.compute(goodsInfoId, (k, v) -> {
//                if (v == null) return new JSONObject();
//                return v;
//            });
//            Long goodsStock = goods.getStock();
//            boolean updateGoodsInfo = false;
//            if(stock != null) {
//                Long goodsInfoStock = goodsInfo.getStock();
//                if(!map.containsKey("stock")) map.put("stock", goodsInfo.getStock().toString());
//                updateGoodsInfo = true;
//                if(goodsStock == null) {
//                    goods.setStock(goodsInfoStock);
//                }else {
//                    int stockDistance = stock - goodsInfoStock.intValue();
//                    long newStock = goodsStock + stockDistance;
//                    goods.setStock(newStock > 0 ? newStock : 0);
//                }
//                goodsInfo.setStock(stock.longValue());
//                redisService.setString(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfoId, stock.toString());
//            }
//            if(price != null){
//                if(!map.containsKey("price")) map.put("price", goodsInfo.getMarketPrice().toString());
//                updateGoodsInfo = true;
//                BigDecimal newPrice = new BigDecimal(price);
//                goodsInfo.setMarketPrice(newPrice);
//            }
//            if(updateGoodsInfo) {
//                toUpdateGoodsInfo.add(goodsInfo);
//            }
//        }
//
//        BigDecimal minPrice = BigDecimal.valueOf(99999);
//        for (GoodsInfo goodsInfo : goodsInfos) {
//            if(goodsInfo.getMarketPrice().compareTo(minPrice) < 0){
//                minPrice = goodsInfo.getMarketPrice();
//            }
//        }
//        goods.setSkuMinMarketPrice(minPrice);
//        if(oldGoodsInfo != null && CollectionUtils.isNotEmpty(liveAssistantGoodsInfos)){
//            wxLiveAssistantGoodsModel.setOldGoodsInfo(JSONObject.toJSONString(oldGoodsInfo));
//            wxLiveAssistantGoodsRepository.save(wxLiveAssistantGoodsModel);
//        }
//        if(CollectionUtils.isNotEmpty(toUpdateGoodsInfo)){
//            goodsService.save(goods);
//            goodsInfoRepository.saveAll(toUpdateGoodsInfo);
//            //微信免审
//            wxGoodsService.toAudit(goods);
//        }
//        return goodsId;
//    }


    /**
     *
     * @param wxLiveAssistantId 直播计划id
     */
    public void openAssistantGoodsValid(Long wxLiveAssistantId) {
        /**
         * 获取直播计划
         */
        Optional<WxLiveAssistantModel> opt = wxLiveAssistantRepository.findById(wxLiveAssistantId);
        if(!opt.isPresent() || opt.get().getDelFlag().equals(DeleteFlag.YES)) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划不存在");
        }
        WxLiveAssistantModel wxLiveAssistantModel = opt.get();
        if (wxLiveAssistantModel.getHasAssistantGoodsValid() != null && wxLiveAssistantModel.getHasAssistantGoodsValid() == HasAssistantGoodsValidEnum.SYNC.getCode()) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "请先关闭同步，才能重新开启");
        }

        /**
         * 获取直播计划商品列表
         */
        List<WxLiveAssistantGoodsModel> assistantGoodsModelList = wxLiveAssistantGoodsRepository.findAll(wxLiveAssistantGoodsRepository
                .buildSearchCondition(WxLiveAssistantSearchRequest.builder().liveAssistantId(wxLiveAssistantId).build()));
        if (CollectionUtils.isEmpty(assistantGoodsModelList)) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划商品为空无法开启");
        }

        this.lockGoodsAndGoodsInfo(assistantGoodsModelList);

        wxLiveAssistantModel.setHasAssistantGoodsValid(HasAssistantGoodsValidEnum.SYNC.getCode());
        wxLiveAssistantRepository.save(wxLiveAssistantModel);
    }


    public void closeAssistantGoodsValid(Long wxLiveAssistantId) {
        /**
         * 获取直播计划
         */
        Optional<WxLiveAssistantModel> opt = wxLiveAssistantRepository.findById(wxLiveAssistantId);
        if(!opt.isPresent() || opt.get().getDelFlag().equals(DeleteFlag.YES)) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划不存在");
        }
        WxLiveAssistantModel wxLiveAssistantModel = opt.get();
        if (wxLiveAssistantModel.getHasAssistantGoodsValid() == null || wxLiveAssistantModel.getHasAssistantGoodsValid() == HasAssistantGoodsValidEnum.NO_SYNC.getCode()) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "请先开启同步，才能重新关闭");
        }

        /**
         * 获取直播计划商品列表
         */
        List<WxLiveAssistantGoodsModel> assistantGoodsModelList = wxLiveAssistantGoodsRepository.findAll(wxLiveAssistantGoodsRepository
                .buildSearchCondition(WxLiveAssistantSearchRequest.builder().liveAssistantId(wxLiveAssistantId).build()));
        if (CollectionUtils.isEmpty(assistantGoodsModelList)) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划商品为空无法关闭");
        }

        this.unlockGoodsAndGoodsInfo(assistantGoodsModelList);

        wxLiveAssistantModel.setHasAssistantGoodsValid(HasAssistantGoodsValidEnum.NO_SYNC.getCode());
        wxLiveAssistantRepository.save(wxLiveAssistantModel);
    }

    /**
     * 获取商品列表
     * @param wxLiveAssistantSearchRequest
     * @return
     */
    public Page<WxLiveAssistantGoodsModel> listGoods(WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest){
        return wxLiveAssistantGoodsRepository.findAll(wxLiveAssistantGoodsRepository.buildSearchCondition(wxLiveAssistantSearchRequest), PageRequest.of(wxLiveAssistantSearchRequest.getPageNum(), wxLiveAssistantSearchRequest.getPageSize()));
    }

    public List<String> ifGoodsInLive(List<String> goodsIds){
        List<WxLiveAssistantGoodsModel> timeConflictGoods = wxLiveAssistantGoodsRepository.findTimeConflictGoods(goodsIds);
        if(CollectionUtils.isNotEmpty(timeConflictGoods)){
            return timeConflictGoods.stream().map(WxLiveAssistantGoodsModel::getGoodsId).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public WxLiveAssistantModel findAssistantById(Long assistantId){
        Optional<WxLiveAssistantModel> opt = wxLiveAssistantRepository.findById(assistantId);
        if(opt.isPresent() && opt.get().getDelFlag().equals(DeleteFlag.NO)) return opt.get();
        return null;
    }

//    @Transactional
    //同步直播计划商品和商品信息

    /**
     * 直播计划开始，库存价格锁定
     * @param wxLiveAssistantGoodsModelList
     * @return
     */
    private void lockGoodsAndGoodsInfo(List<WxLiveAssistantGoodsModel> wxLiveAssistantGoodsModelList){

        List<String> goodsIdList = new ArrayList<>();
        Map<String, WxLiveAssistantGoodsModel> goodsId2AssistantGoodsMap = new HashMap<>();
        for (WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel : wxLiveAssistantGoodsModelList) {
            goodsIdList.add(wxLiveAssistantGoodsModel.getGoodsId());
            goodsId2AssistantGoodsMap.put(wxLiveAssistantGoodsModel.getGoodsId(), wxLiveAssistantGoodsModel);
        }

        List<GoodsInfo> goodsInfoList = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().goodsIds(goodsIdList).delFlag(DeleteFlag.NO.toValue()).build());
        if (CollectionUtils.isEmpty(goodsInfoList)) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED,  "直播计划下商品为空");
        }
        Map<String, WxLiveAssistantGoodsConfigVo> goodsId2AssistantGoodsConfigMap = new HashMap<>();

        for (GoodsInfo goodsInfo : goodsInfoList) {
            WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel = goodsId2AssistantGoodsMap.get(goodsInfo.getGoodsId());
            if (wxLiveAssistantGoodsModel == null) {
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED,  "goodsInfoNo:" + goodsInfo.getGoodsInfoNo() + "需要删除重新新增一下");
            }

            String newGoodsInfoJson = wxLiveAssistantGoodsModel.getNewGoodsInfoJson();
            if (StringUtils.isEmpty(newGoodsInfoJson)) {
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED,  "goodsInfoNo:" + goodsInfo.getGoodsInfoNo() + "没有新增进去商品价格库存信息，请删除重新新增");
            }

            List<WxLiveAssistantGoodsInfoConfigVo> assistantGoodsInfoConfigVoList = JSON.parseArray(newGoodsInfoJson, WxLiveAssistantGoodsInfoConfigVo.class);
            Map<String, WxLiveAssistantGoodsInfoConfigVo> goodsInfoId2AssistantGoodsInfoConfigMap =
                    assistantGoodsInfoConfigVoList.stream().collect(Collectors.toMap(WxLiveAssistantGoodsInfoConfigVo::getGoodsInfoId, Function.identity(), (k1, k2) -> k1));
            WxLiveAssistantGoodsInfoConfigVo wxLiveAssistantGoodsInfoConfigVo = goodsInfoId2AssistantGoodsInfoConfigMap.get(goodsInfo.getGoodsInfoId());
            if (wxLiveAssistantGoodsInfoConfigVo == null) {
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED,  "goodsInfoNo:" + goodsInfo.getGoodsInfoNo() + "没有新增进去商品价格库存信息，请删除重新新增");
            }

            WxLiveAssistantGoodsConfigVo assistantGoodsConfigVo = goodsId2AssistantGoodsConfigMap.get(goodsInfo.getGoodsId());
            if (assistantGoodsConfigVo == null) {
                assistantGoodsConfigVo = new WxLiveAssistantGoodsConfigVo();
                assistantGoodsConfigVo.setGoodsId(goodsInfo.getGoodsId());
                assistantGoodsConfigVo.setSumStock(0L);
                assistantGoodsConfigVo.setSkuMiniMarketPrice(new BigDecimal("999999"));
                goodsId2AssistantGoodsConfigMap.put(goodsInfo.getGoodsId(), assistantGoodsConfigVo);
            }

            //设置goods的最小金额
            if (wxLiveAssistantGoodsInfoConfigVo.getWxPrice().compareTo(assistantGoodsConfigVo.getSkuMiniMarketPrice()) < 0) {
                assistantGoodsConfigVo.setSkuMiniMarketPrice(wxLiveAssistantGoodsInfoConfigVo.getWxPrice());
            }
            //库存累加
            assistantGoodsConfigVo.setSumStock(assistantGoodsConfigVo.getSumStock() + wxLiveAssistantGoodsInfoConfigVo.getStock());


            goodsInfo.setMarketPrice(wxLiveAssistantGoodsInfoConfigVo.getWxPrice());
            goodsInfo.setStock(wxLiveAssistantGoodsInfoConfigVo.getStock());
            goodsInfo.setStockSyncFlag(0); //强制不进行同步

//            goodsInfoRepository.updateMarketPriceAndStockAndStockSyncFlagById(wxLiveAssistantGoodsInfoConfigVo.getWxPrice(), wxLiveAssistantGoodsInfoConfigVo.getStock(), 0, goodsInfo.getGoodsInfoId());
        }

        goodsInfoRepository.saveAll(goodsInfoList);

        //更新goods
        goodsId2AssistantGoodsConfigMap.forEach((K, V) -> {
            goodsRepository.updateStockAndSkuMinMarketPriceByGoodsId(V.getSumStock(), V.getSkuMiniMarketPrice(), V.getGoodsId());
        });

        //刷新es
    }



    /**
     * 直播计划结束，库存价格释放
     * @param wxLiveAssistantGoodsModelList
     * @return
     */
    private void unlockGoodsAndGoodsInfo(List<WxLiveAssistantGoodsModel> wxLiveAssistantGoodsModelList){

        List<String> goodsIdList = new ArrayList<>();
        Map<String, WxLiveAssistantGoodsModel> goodsId2AssistantGoodsMap = new HashMap<>();
        for (WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel : wxLiveAssistantGoodsModelList) {
            goodsIdList.add(wxLiveAssistantGoodsModel.getGoodsId());
            goodsId2AssistantGoodsMap.put(wxLiveAssistantGoodsModel.getGoodsId(), wxLiveAssistantGoodsModel);
        }



        List<GoodsInfo> goodsInfoList = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().goodsIds(goodsIdList).delFlag(DeleteFlag.NO.toValue()).build());

        //goodsId对应的商品库存价格信息
        Map<String, WxLiveAssistantGoodsConfigVo> goodsId2AssistantGoodsConfigMap = new HashMap<>();

        for (GoodsInfo goodsInfo : goodsInfoList) {
            WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel = goodsId2AssistantGoodsMap.get(goodsInfo.getGoodsId());
            if (wxLiveAssistantGoodsModel == null) {
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED,  "goodsInfoNo:" + goodsInfo.getGoodsInfoNo() + "需要删除重新新增一下");
            }

            String oldGoodsInfoStr = wxLiveAssistantGoodsModel.getOldGoodsInfo();
            String olfSyncStockFlagStr = wxLiveAssistantGoodsModel.getOlfSyncStockFlag();
            if (StringUtils.isBlank(oldGoodsInfoStr) || StringUtils.isBlank(olfSyncStockFlagStr)) {
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED,  "goodsInfoNo:" + goodsInfo.getGoodsInfoNo() + "没有新增进去商品价格库存信息，请删除重新新增");
            }

            WxLiveAssistantGoodsConfigVo assistantGoodsConfigVo = goodsId2AssistantGoodsConfigMap.get(goodsInfo.getGoodsId());
            if (assistantGoodsConfigVo == null) {
                assistantGoodsConfigVo = new WxLiveAssistantGoodsConfigVo();
                assistantGoodsConfigVo.setGoodsId(goodsInfo.getGoodsId());
                assistantGoodsConfigVo.setSumStock(0L);
                assistantGoodsConfigVo.setSkuMiniMarketPrice(new BigDecimal("999999"));
                goodsId2AssistantGoodsConfigMap.put(goodsInfo.getGoodsId(), assistantGoodsConfigVo);
            }


            //价格还原
            Map oldGoodsInfoMap = null;
            if (StringUtils.isNotBlank(oldGoodsInfoStr)) {
                oldGoodsInfoMap = JSONObject.parseObject(oldGoodsInfoStr, Map.class);
            }
            Object oldGoodsInfoMapObj = oldGoodsInfoMap.get(goodsInfo.getGoodsInfoId());
            if (oldGoodsInfoMapObj != null) {
                Map<String, BigDecimal> oldGoodsInfoInnerMap = (Map<String, BigDecimal>) oldGoodsInfoMapObj;
                BigDecimal oldGoodsInfoMarketPrice = oldGoodsInfoInnerMap.get("price");
                if (oldGoodsInfoMarketPrice != null) {
                    goodsInfo.setMarketPrice(oldGoodsInfoMarketPrice);

                    //设置goods的最小金额
                    if (oldGoodsInfoMarketPrice.compareTo(assistantGoodsConfigVo.getSkuMiniMarketPrice()) < 0) {
                        assistantGoodsConfigVo.setSkuMiniMarketPrice(oldGoodsInfoMarketPrice);
                    }
                }
            }

            //设置下架
            goodsInfo.setAddedFlag(AddedFlag.NO.toValue());

            // 设置库存同步还原
            if (StringUtils.isNotBlank(olfSyncStockFlagStr)) {
                Map oldSyncStockMap = JSONObject.parseObject(olfSyncStockFlagStr, Map.class);
                if (oldSyncStockMap != null) {
                    Object syncStockFlagObj = oldSyncStockMap.get(goodsInfo.getGoodsInfoId());
                    if (syncStockFlagObj != null) {
                        goodsInfo.setStockSyncFlag(Integer.valueOf(syncStockFlagObj.toString()));
                        //goods的库存不同还原，因为需要同步库存信息
                    }
                }
            }

        }

        goodsInfoRepository.saveAll(goodsInfoList);

        //更新goods 只是还原价格, 同时下架商品
        goodsId2AssistantGoodsConfigMap.forEach((K, V) -> {
            goodsRepository.updateSkuMinMarketPriceByGoodsId(AddedFlag.NO.toValue(), V.getSkuMiniMarketPrice(), V.getGoodsId());
        });

        //同步库存 不判断是否自动同步，交给同步方法处理 此处只是处理管易云的商品
        goodsStockService.batchUpdateStock(
                wxLiveAssistantGoodsModelList.stream().map(WxLiveAssistantGoodsModel::getGoodsId).distinct().collect(Collectors.toList()), "", 0, 0);
    }

//    @Transactional
//    public List<Goods> resetStockAndProce(Long wxLiveAssistantId){
//        List<WxLiveAssistantGoodsModel> assistantGoodsModels = wxLiveAssistantGoodsRepository.findAll(wxLiveAssistantGoodsRepository
//                .buildSearchCondition(WxLiveAssistantSearchRequest.builder().liveAssistantId(wxLiveAssistantId).build()));
//        Map<String, List<WxLiveAssistantGoodsModel>> assistantGoodsGroup = assistantGoodsModels.stream().collect(Collectors.groupingBy(WxLiveAssistantGoodsModel::getGoodsId));
//        List<GoodsInfo> goodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().goodsIds(new ArrayList<>(assistantGoodsGroup.keySet())).delFlag(DeleteFlag.NO.toValue()).build());
//        Map<String, List<GoodsInfo>> goodsInfoGroup = goodsInfos.stream().collect(Collectors.groupingBy(GoodsInfo::getGoodsId));
//
////        Map<String, Map<String, Integer>> resultMap = new HashMap<>();
////        Map<String, Integer> skus = new HashMap<>();
////        Map<String, Integer> spus = new HashMap<>();
//        List<Goods> goodsList = new ArrayList<>();
//
//        List<GoodsInfo> toSave = new ArrayList<>();
//        for (List<WxLiveAssistantGoodsModel> value : assistantGoodsGroup.values()) {
//            WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel = value.get(0);
//            List<Goods> goodsList2 = resetStockAndProce(wxLiveAssistantGoodsModel, goodsInfoGroup.get(wxLiveAssistantGoodsModel.getGoodsId()), toSave);
//            goodsList.addAll(goodsList2);
////            Map<String, Integer> skus1 = map.get("skus");
////            Map<String, Integer> spus1 = map.get("spus");
////            skus1.forEach((k ,v) -> {
////                skus.compute(k, (k2, v2) -> {
////                    if(v2 == null){
////                        return v;
////                    }else {
////                        return v + v2;
////                    }
////                });
////            });
////            spus1.forEach((k, v) -> {
////                spus.compute(k, (k2, v2) -> {
////                    if(v2 == null){
////                        return v;
////                    }else {
////                        return v + v2;
////                    }
////                });
////            });
//        }
//        if(!toSave.isEmpty()){
////            goodsInfoRepository.saveAll(toSave);
////            goodsRepository.saveAll(goodsList);
//        }
////        resultMap.put("spus", spus);
////        resultMap.put("skus", skus);
//        return goodsList;
//    }

////    private Map<String, Map<String, Integer>> resetStockAndProce(WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel, List<GoodsInfo> goodsInfos, List<GoodsInfo> toSave){
//    private List<Goods> resetStockAndProce(WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel, List<GoodsInfo> goodsInfos, List<GoodsInfo> toSave){
//        String olfSyncStockFlagStr = wxLiveAssistantGoodsModel.getOlfSyncStockFlag();
//        JSONObject olfSyncStockFlag = JSONObject.parseObject(olfSyncStockFlagStr);
//        String oldGoodsInfoStr = wxLiveAssistantGoodsModel.getOldGoodsInfo();
//        Map oldGoodsInfoMap = null;
//        if (StringUtils.isNotBlank(oldGoodsInfoStr)) {
//            oldGoodsInfoMap = JSONObject.parseObject(oldGoodsInfoStr, Map.class);
//        }
//
////        Map<String, Map<String, Integer>> resultMap = new HashMap<>();
////        Map<String, Integer> skus = new HashMap<>();
////        Map<String, Integer> spus = new HashMap<>();
//        List<Goods> goodsList = new ArrayList<>();
//
//        String goodsId = goodsInfos.get(0).getGoodsId();
//        Goods goods = goodsService.findByGoodsId(goodsId);
//        goodsList.add(goods);
////        boolean goodsChange = false;
//        for (GoodsInfo goodsInfo : goodsInfos) {
////            boolean change = false;
//            Integer oldFlag = (Integer) olfSyncStockFlag.get(goodsInfo.getGoodsInfoId());
////            BigDecimal skuMinMarketPrice = goods.getSkuMinMarketPrice();
//
//            if (oldGoodsInfoMap != null) {
//                Object obj = oldGoodsInfoMap.get(goodsInfo.getGoodsInfoId());
//                if (obj != null) {
//                    Map<String, String> oldInfo = (Map<String, String>) obj;
//                    String price = oldInfo.get("price");
//                    if(price != null) {
//                        goodsInfo.setMarketPrice(new BigDecimal(price));
//                    }
//                }
//            }
//
//            oldFlag = oldFlag == null ? 0 : oldFlag;
//            goodsInfo.setStockSyncFlag(oldFlag);
//            goodsInfoRepository.save(goodsInfo);
//
//            if(oldFlag == null || oldFlag == 0){
//                //开播之前未开同步
//
//            }else {
//                //开播之前同步了,同步一次库存
////                change = true;
////                goodsInfo.setStockSyncFlag(1);
////                Map<String, Map<String, Integer>> map = goodsStockService.partialUpdateStock(goodsInfo.getErpGoodsInfoNo(), "", "1", "10");
////                goodsStockService.partialUpdateStock(goodsInfo.getErpGoodsInfoNo(), "", "1", "10");
//                goodsStockService.batchUpdateStock(Collections.singletonList(goodsInfo.getGoodsId()), "", 0, 80);
////                goodsStockService.batchUpdateStock()
////                Map<String, Integer> skus1 = map.get("skus");
////                Map<String, Integer> spus1 = map.get("spus");
////                skus1.forEach((k ,v) -> {
////                    skus.compute(k, (k2, v2) -> {
////                        if(v2 == null){
////                            return v;
////                        }else {
////                            return v + v2;
////                        }
////                    });
////                });
////                spus1.forEach((k, v) -> {
////                    spus.compute(k, (k2, v2) -> {
////                        if(v2 == null){
////                            return v;
////                        }else {
////                            return v + v2;
////                        }
////                    });
////                });
//            }
////            if(oldGoodsInfo != null){
////                Map<String, String> oldInfo = (Map<String, String>) oldGoodsInfo.get(goodsInfo.getGoodsInfoId());
////                if(oldInfo != null){
////                    String price = oldInfo.get("price");
////                    if(price != null){
////                        change = true;
////                        goodsChange = true;
////                        goodsInfo.setMarketPrice(new BigDecimal(price));
////                    }
////                }
////            }
////            if(change){
////                toSave.add(goodsInfo);
////            }
//        }
////        if(goodsChange){
////            goodsList.add(goods);
////            BigDecimal minPrice = BigDecimal.valueOf(99999);
////            for (GoodsInfo goodsInfo : goodsInfos) {
////                if(goodsInfo.getMarketPrice().compareTo(minPrice) < 0){
////                    minPrice = goodsInfo.getMarketPrice();
////                }
////            }
////            goods.setSkuMinMarketPrice(minPrice);
////        }
////        resultMap.put("spus", spus);
////        resultMap.put("skus", skus);
//        return goodsList;
//    }

}
