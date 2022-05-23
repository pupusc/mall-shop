package com.wanmi.sbc.goods.mini.service.assistant;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.enums.HasAssistantGoodsValidEnum;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantGoodsCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantGoodsUpdateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantSearchRequest;
import com.wanmi.sbc.goods.bean.wx.vo.assistant.WxLiveAssistantGoodsInfoConfigVo;
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
    public List<Goods> deleteAssistant(Long id){
        Optional<WxLiveAssistantModel> opt = wxLiveAssistantRepository.findById(id);
        if(!opt.isPresent() || opt.get().getDelFlag().equals(DeleteFlag.YES)) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划不存在");
        WxLiveAssistantModel wxLiveAssistantModel = opt.get();
        if(wxLiveAssistantModel.getEndTime().isBefore(LocalDateTime.now())) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播已结束，不能删除");
        wxLiveAssistantModel.setDelFlag(DeleteFlag.YES);
        wxLiveAssistantModel.setUpdateTime(LocalDateTime.now());
        wxLiveAssistantRepository.save(wxLiveAssistantModel);

        log.info("删除直播助手:{}, 所有直播商品将恢复原价和库存", id);
        return resetStockAndProce(id);
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
        Map<String, String> resultMap = new HashMap<>();
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
    public void addGoods(WxLiveAssistantGoodsCreateRequest wxLiveAssistantGoodsCreateRequest){
        Long assistantId = wxLiveAssistantGoodsCreateRequest.getAssistantId();
        if (assistantId == null) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划id不能为空");
        Optional<WxLiveAssistantModel> opt = wxLiveAssistantRepository.findById(assistantId);
        if(!opt.isPresent() || opt.get().getDelFlag().equals(DeleteFlag.YES)) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划不存在");
        if(CollectionUtils.isEmpty(wxLiveAssistantGoodsCreateRequest.getGoods())) return;

        WxLiveAssistantModel wxLiveAssistantModel = opt.get();
        addGoods(wxLiveAssistantGoodsCreateRequest, wxLiveAssistantModel);
    }


    @Transactional
    public void addGoods(WxLiveAssistantGoodsCreateRequest wxLiveAssistantGoodsCreateRequest, WxLiveAssistantModel wxLiveAssistantModel){

        if(wxLiveAssistantModel.getEndTime().isBefore(LocalDateTime.now())) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播已结束，不能修改");

        List<WxLiveAssistantGoodsModel> timeConflictGoods = wxLiveAssistantGoodsRepository.findTimeConflictGoods(wxLiveAssistantGoodsCreateRequest.getGoods());
        if(CollectionUtils.isNotEmpty(timeConflictGoods)){
            List<String> timeConflictGoodsIds = timeConflictGoods.stream().map(WxLiveAssistantGoodsModel::getGoodsId).collect(Collectors.toList());
            List<Goods> goodsList = goodsRepository.findAllByGoodsIdIn(timeConflictGoodsIds);
            List<String> goodsNoList = goodsList.stream().map(Goods::getGoodsNo).collect(Collectors.toList());
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, Arrays.toString(goodsNoList.toArray()) + " 已添加至其他直播计划，无法重复添加");
        }
        wxLiveAssistantModel.setGoodsCount(wxLiveAssistantModel.getGoodsCount() + wxLiveAssistantGoodsCreateRequest.getGoods().size());
        wxLiveAssistantRepository.save(wxLiveAssistantModel);

        List<WxLiveAssistantGoodsModel> assistantGoodsModels = WxLiveAssistantGoodsModel.create(wxLiveAssistantGoodsCreateRequest);
        wxLiveAssistantGoodsRepository.saveAll(assistantGoodsModels);

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
        wxLiveAssistantGoodsModel.setDelFlag(DeleteFlag.YES);
        wxLiveAssistantGoodsModel.setUpdateTime(LocalDateTime.now());

        Optional<WxLiveAssistantModel> opt2 = wxLiveAssistantRepository.findById(wxLiveAssistantGoodsModel.getAssistId());
        if(!opt2.isPresent() || opt2.get().getDelFlag().equals(DeleteFlag.YES)){
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划不存在");
        }
        WxLiveAssistantModel wxLiveAssistantModel = opt2.get();
        if(wxLiveAssistantModel.getEndTime().isBefore(LocalDateTime.now())) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播已结束，不能修改");
        wxLiveAssistantModel.setGoodsCount(wxLiveAssistantModel.getGoodsCount() - 1);
        wxLiveAssistantRepository.save(wxLiveAssistantModel);
        wxLiveAssistantGoodsRepository.save(wxLiveAssistantGoodsModel);

        String goodsId = wxLiveAssistantGoodsModel.getGoodsId();
        log.info("直播计划：{} 直播商品 {}删除，关联的sku将恢复库存同步和价格", wxLiveAssistantModel.getId(), goodsId);
//        List<GoodsInfo> goodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().goodsIds(Collections.singletonList(goodsId)).delFlag(DeleteFlag.NO.toValue()).build());
//        List<GoodsInfo> toSave = new ArrayList<>();
//        List<Goods> goodsList = resetStockAndProce(wxLiveAssistantGoodsModel, goodsInfos, toSave);
//        if (!toSave.isEmpty()) {
////            goodsInfoRepository.saveAll(toSave);
////            goodsRepository.saveAll(goodsList);
//        }
        return Collections.singletonList(goodsId);
    }

    @Transactional
    public String updateGoodsInfos(WxLiveAssistantGoodsUpdateRequest wxLiveAssistantGoodsUpdateRequest){
        Optional<WxLiveAssistantModel> opt = wxLiveAssistantRepository.findById(wxLiveAssistantGoodsUpdateRequest.getAssistantId());
        if(!opt.isPresent() || opt.get().getDelFlag().equals(DeleteFlag.YES)) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划不存在");
        WxLiveAssistantModel wxLiveAssistantModel = opt.get();
        if(wxLiveAssistantModel.getEndTime().isBefore(LocalDateTime.now())) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播已结束，不能修改");

        Optional<WxLiveAssistantGoodsModel> opt2 = wxLiveAssistantGoodsRepository.findById(wxLiveAssistantGoodsUpdateRequest.getAssistantGoodsId());
        if(!opt2.isPresent() || opt2.get().getDelFlag().equals(DeleteFlag.YES)) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播商品不存在");
        if(!opt2.get().getAssistId().equals(wxLiveAssistantGoodsUpdateRequest.getAssistantId())) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划不存在");

        List<WxLiveAssistantGoodsUpdateRequest.WxLiveAssistantGoodsInfo> liveAssistantGoodsInfos = wxLiveAssistantGoodsUpdateRequest.getGoodsInfos();
//        List<String> goodsInfoIds = liveAssistantGoodsInfos.stream().map(WxLiveAssistantGoodsUpdateRequest.WxLiveAssistantGoodsInfo::getGoodsInfoId).collect(Collectors.toList());
//        List<GoodsInfo> goodsInfos = goodsInfoService.findByIds(goodsInfoIds);
//        Map<String, List<GoodsInfo>> goodsInfoGroup = goodsInfos.stream().filter(g -> g.getDelFlag().equals(DeleteFlag.NO)).collect(Collectors.groupingBy(GoodsInfo::getGoodsInfoId));
        WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel = opt2.get();
        String goodsId = wxLiveAssistantGoodsModel.getGoodsId();
        Goods goods = goodsService.findByGoodsId(goodsId);

        List<GoodsInfo> goodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().goodsIds(Collections.singletonList(goodsId)).delFlag(DeleteFlag.NO.toValue()).build());
        Map<String, List<GoodsInfo>> goodsInfoGroup = goodsInfos.stream().filter(g -> g.getDelFlag().equals(DeleteFlag.NO)).collect(Collectors.groupingBy(GoodsInfo::getGoodsInfoId));

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
            if(!goodsId.equals(goodsInfo.getGoodsId())) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "非法参数");
            JSONObject map = (JSONObject) oldGoodsInfo.compute(goodsInfoId, (k, v) -> {
                if (v == null) return new JSONObject();
                return v;
            });
            Long goodsStock = goods.getStock();
            boolean updateGoodsInfo = false;
            if(stock != null) {
                Long goodsInfoStock = goodsInfo.getStock();
                if(!map.containsKey("stock")) map.put("stock", goodsInfo.getStock().toString());
                updateGoodsInfo = true;
                if(goodsStock == null) {
                    goods.setStock(goodsInfoStock);
                }else {
                    int stockDistance = stock - goodsInfoStock.intValue();
                    long newStock = goodsStock + stockDistance;
                    goods.setStock(newStock > 0 ? newStock : 0);
                }
                goodsInfo.setStock(stock.longValue());
                redisService.setString(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfoId, stock.toString());
            }
            if(price != null){
                if(!map.containsKey("price")) map.put("price", goodsInfo.getMarketPrice().toString());
                updateGoodsInfo = true;
                BigDecimal newPrice = new BigDecimal(price);
                goodsInfo.setMarketPrice(newPrice);
            }
            if(updateGoodsInfo) {
                toUpdateGoodsInfo.add(goodsInfo);
            }
        }

        BigDecimal minPrice = BigDecimal.valueOf(99999);
        for (GoodsInfo goodsInfo : goodsInfos) {
            if(goodsInfo.getMarketPrice().compareTo(minPrice) < 0){
                minPrice = goodsInfo.getMarketPrice();
            }
        }
        goods.setSkuMinMarketPrice(minPrice);
        if(oldGoodsInfo != null && CollectionUtils.isNotEmpty(liveAssistantGoodsInfos)){
            wxLiveAssistantGoodsModel.setOldGoodsInfo(JSONObject.toJSONString(oldGoodsInfo));
            wxLiveAssistantGoodsRepository.save(wxLiveAssistantGoodsModel);
        }
        if(CollectionUtils.isNotEmpty(toUpdateGoodsInfo)){
            goodsService.save(goods);
            goodsInfoRepository.saveAll(toUpdateGoodsInfo);
            //微信免审
            wxGoodsService.toAudit(goods);
        }
        return goodsId;
    }


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

        /**
         * 获取直播计划商品列表
         */
        List<WxLiveAssistantGoodsModel> assistantGoodsModelList = wxLiveAssistantGoodsRepository.findAll(wxLiveAssistantGoodsRepository
                .buildSearchCondition(WxLiveAssistantSearchRequest.builder().liveAssistantId(wxLiveAssistantId).build()));
        if (CollectionUtils.isEmpty(assistantGoodsModelList)) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划商品为空无法开启");
        }

        //批量获取商品信息
        List<String> goodsIdList = assistantGoodsModelList.stream().map(WxLiveAssistantGoodsModel::getGoodsId).collect(Collectors.toList());
        List<GoodsInfo> goodsInfoList = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().goodsIds(goodsIdList).delFlag(DeleteFlag.NO.toValue()).build());
        if (CollectionUtils.isEmpty(goodsInfoList)) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划商品为空无法开启");
        }

        //直播计划商品库存同步
        Map<String, Map<String, Integer>> goodsId2GoodsInfoId2StockFlagSyncMap = new HashMap<>(goodsInfoList.size());
        //直播计划商品价格
        Map<String, Map<String, BigDecimal>> goodsId2GoodsInfoId2PriceMap = new HashMap<>(goodsInfoList.size());

//        Map<String, GoodsInfo> goodsInfoMap = new HashMap<>();

        for (GoodsInfo goodsInfo : goodsInfoList) {
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

//            goodsInfoMap.put(goodsInfo.getGoodsInfoId(), goodsInfo);
        }

        Map<String, WxLiveAssistantGoodsInfoConfigVo> goodsInfoId2AssistantGoodsInfoConfigMap = new HashMap<>();
        //更新商品价格，库存同步标志
        for (WxLiveAssistantGoodsModel assistantGoodsModel : assistantGoodsModelList) {
            Map<String, Integer> goodsInfo2StockFlagSyncMap = goodsId2GoodsInfoId2StockFlagSyncMap.get(assistantGoodsModel.getGoodsId());
            if (goodsInfo2StockFlagSyncMap != null) {
                assistantGoodsModel.setOlfSyncStockFlag(JSON.toJSONString(goodsInfo2StockFlagSyncMap));
            }

            Map<String, BigDecimal> goodsInfo2PriceMap = goodsId2GoodsInfoId2PriceMap.get(assistantGoodsModel.getGoodsId());
            if (goodsInfo2PriceMap != null) {
                assistantGoodsModel.setOldGoodsInfo(JSON.toJSONString(goodsInfo2PriceMap));
            }

            String newGoodsInfoJson = assistantGoodsModel.getNewGoodsInfoJson();
            if (StringUtils.isEmpty(newGoodsInfoJson)) {
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED,  assistantGoodsModel.getGoodsId() + "需要重新更新一下价格和库存");
            } else {
                List<WxLiveAssistantGoodsInfoConfigVo> wxLiveAssistantGoodsConfigVoList =
                        JSONArray.parseArray(newGoodsInfoJson, WxLiveAssistantGoodsInfoConfigVo.class);
                for (WxLiveAssistantGoodsInfoConfigVo wxLiveAssistantGoodsInfoConfigVo : wxLiveAssistantGoodsConfigVoList) {
                    goodsInfoId2AssistantGoodsInfoConfigMap.put(wxLiveAssistantGoodsInfoConfigVo.getGoodsInfoId(), wxLiveAssistantGoodsInfoConfigVo);
                }
            }

        }
        //更新直播计划商品信息
        wxLiveAssistantGoodsRepository.saveAll(assistantGoodsModelList);

        //更新直播计划为有效
        wxLiveAssistantModel.setHasAssistantGoodsValid(HasAssistantGoodsValidEnum.SYNC.getCode());
        wxLiveAssistantRepository.save(wxLiveAssistantModel);


        //同步goodsInfo信息
        for (GoodsInfo goodsInfo : goodsInfoList) {
            WxLiveAssistantGoodsInfoConfigVo wxLiveAssistantGoodsInfoConfigVo = goodsInfoId2AssistantGoodsInfoConfigMap.get(goodsInfo.getGoodsInfoId());
            if (wxLiveAssistantGoodsInfoConfigVo != null) {
                goodsInfo.setMarketPrice(wxLiveAssistantGoodsInfoConfigVo.getWxPrice());
                goodsInfo.setStock(wxLiveAssistantGoodsInfoConfigVo.getStock().longValue());
            }
        }
        goodsInfoRepository.saveAll(goodsInfoList);
        //同步goods信息
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

    @Transactional
    public List<Goods> resetStockAndProce(Long wxLiveAssistantId){
        List<WxLiveAssistantGoodsModel> assistantGoodsModels = wxLiveAssistantGoodsRepository.findAll(wxLiveAssistantGoodsRepository
                .buildSearchCondition(WxLiveAssistantSearchRequest.builder().liveAssistantId(wxLiveAssistantId).build()));
        Map<String, List<WxLiveAssistantGoodsModel>> assistantGoodsGroup = assistantGoodsModels.stream().collect(Collectors.groupingBy(WxLiveAssistantGoodsModel::getGoodsId));
        List<GoodsInfo> goodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().goodsIds(new ArrayList<>(assistantGoodsGroup.keySet())).delFlag(DeleteFlag.NO.toValue()).build());
        Map<String, List<GoodsInfo>> goodsInfoGroup = goodsInfos.stream().collect(Collectors.groupingBy(GoodsInfo::getGoodsId));

//        Map<String, Map<String, Integer>> resultMap = new HashMap<>();
//        Map<String, Integer> skus = new HashMap<>();
//        Map<String, Integer> spus = new HashMap<>();
        List<Goods> goodsList = new ArrayList<>();

        List<GoodsInfo> toSave = new ArrayList<>();
        for (List<WxLiveAssistantGoodsModel> value : assistantGoodsGroup.values()) {
            WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel = value.get(0);
            List<Goods> goodsList2 = resetStockAndProce(wxLiveAssistantGoodsModel, goodsInfoGroup.get(wxLiveAssistantGoodsModel.getGoodsId()), toSave);
            goodsList.addAll(goodsList2);
//            Map<String, Integer> skus1 = map.get("skus");
//            Map<String, Integer> spus1 = map.get("spus");
//            skus1.forEach((k ,v) -> {
//                skus.compute(k, (k2, v2) -> {
//                    if(v2 == null){
//                        return v;
//                    }else {
//                        return v + v2;
//                    }
//                });
//            });
//            spus1.forEach((k, v) -> {
//                spus.compute(k, (k2, v2) -> {
//                    if(v2 == null){
//                        return v;
//                    }else {
//                        return v + v2;
//                    }
//                });
//            });
        }
        if(!toSave.isEmpty()){
//            goodsInfoRepository.saveAll(toSave);
//            goodsRepository.saveAll(goodsList);
        }
//        resultMap.put("spus", spus);
//        resultMap.put("skus", skus);
        return goodsList;
    }

//    private Map<String, Map<String, Integer>> resetStockAndProce(WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel, List<GoodsInfo> goodsInfos, List<GoodsInfo> toSave){
    private List<Goods> resetStockAndProce(WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel, List<GoodsInfo> goodsInfos, List<GoodsInfo> toSave){
        String olfSyncStockFlagStr = wxLiveAssistantGoodsModel.getOlfSyncStockFlag();
        JSONObject olfSyncStockFlag = JSONObject.parseObject(olfSyncStockFlagStr);
        String oldGoodsInfoStr = wxLiveAssistantGoodsModel.getOldGoodsInfo();
        Map oldGoodsInfoMap = null;
        if (StringUtils.isNotBlank(oldGoodsInfoStr)) {
            oldGoodsInfoMap = JSONObject.parseObject(oldGoodsInfoStr, Map.class);
        }

//        Map<String, Map<String, Integer>> resultMap = new HashMap<>();
//        Map<String, Integer> skus = new HashMap<>();
//        Map<String, Integer> spus = new HashMap<>();
        List<Goods> goodsList = new ArrayList<>();

        String goodsId = goodsInfos.get(0).getGoodsId();
        Goods goods = goodsService.findByGoodsId(goodsId);
        goodsList.add(goods);
//        boolean goodsChange = false;
        for (GoodsInfo goodsInfo : goodsInfos) {
//            boolean change = false;
            Integer oldFlag = (Integer) olfSyncStockFlag.get(goodsInfo.getGoodsInfoId());
//            BigDecimal skuMinMarketPrice = goods.getSkuMinMarketPrice();

            if (oldGoodsInfoMap != null) {
                Object obj = oldGoodsInfoMap.get(goodsInfo.getGoodsInfoId());
                if (obj != null) {
                    Map<String, String> oldInfo = (Map<String, String>) obj;
                    String price = oldInfo.get("price");
                    if(price != null) {
                        goodsInfo.setMarketPrice(new BigDecimal(price));
                    }
                }
            }

            oldFlag = oldFlag == null ? 0 : oldFlag;
            goodsInfo.setStockSyncFlag(oldFlag);
            goodsInfoRepository.save(goodsInfo);

            if(oldFlag == null || oldFlag == 0){
                //开播之前未开同步

            }else {
                //开播之前同步了,同步一次库存
//                change = true;
//                goodsInfo.setStockSyncFlag(1);
//                Map<String, Map<String, Integer>> map = goodsStockService.partialUpdateStock(goodsInfo.getErpGoodsInfoNo(), "", "1", "10");
//                goodsStockService.partialUpdateStock(goodsInfo.getErpGoodsInfoNo(), "", "1", "10");
                goodsStockService.batchUpdateStock(Collections.singletonList(goodsInfo.getGoodsId()), "", 0, 80);
//                goodsStockService.batchUpdateStock()
//                Map<String, Integer> skus1 = map.get("skus");
//                Map<String, Integer> spus1 = map.get("spus");
//                skus1.forEach((k ,v) -> {
//                    skus.compute(k, (k2, v2) -> {
//                        if(v2 == null){
//                            return v;
//                        }else {
//                            return v + v2;
//                        }
//                    });
//                });
//                spus1.forEach((k, v) -> {
//                    spus.compute(k, (k2, v2) -> {
//                        if(v2 == null){
//                            return v;
//                        }else {
//                            return v + v2;
//                        }
//                    });
//                });
            }
//            if(oldGoodsInfo != null){
//                Map<String, String> oldInfo = (Map<String, String>) oldGoodsInfo.get(goodsInfo.getGoodsInfoId());
//                if(oldInfo != null){
//                    String price = oldInfo.get("price");
//                    if(price != null){
//                        change = true;
//                        goodsChange = true;
//                        goodsInfo.setMarketPrice(new BigDecimal(price));
//                    }
//                }
//            }
//            if(change){
//                toSave.add(goodsInfo);
//            }
        }
//        if(goodsChange){
//            goodsList.add(goods);
//            BigDecimal minPrice = BigDecimal.valueOf(99999);
//            for (GoodsInfo goodsInfo : goodsInfos) {
//                if(goodsInfo.getMarketPrice().compareTo(minPrice) < 0){
//                    minPrice = goodsInfo.getMarketPrice();
//                }
//            }
//            goods.setSkuMinMarketPrice(minPrice);
//        }
//        resultMap.put("spus", spus);
//        resultMap.put("skus", skus);
        return goodsList;
    }

}
