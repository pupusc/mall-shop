package com.wanmi.sbc.goods.provider.impl.mini.assistant;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.provider.mini.assistant.WxLiveAssistantProvider;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantGoodsCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantGoodsUpdateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantSearchRequest;
import com.wanmi.sbc.goods.bean.wx.vo.assistant.WxLiveAssistantDetailVo;
import com.wanmi.sbc.goods.bean.wx.vo.assistant.WxLiveAssistantGoodsVo;
import com.wanmi.sbc.goods.bean.wx.vo.assistant.WxLiveAssistantVo;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import com.wanmi.sbc.goods.info.service.GoodsService;
import com.wanmi.sbc.goods.mini.model.goods.WxLiveAssistantGoodsModel;
import com.wanmi.sbc.goods.mini.model.goods.WxLiveAssistantModel;
import com.wanmi.sbc.goods.mini.service.assistant.WxLiveAssistantService;
import com.wanmi.sbc.goods.spec.model.root.GoodsInfoSpecDetailRel;
import com.wanmi.sbc.goods.spec.model.root.GoodsSpec;
import com.wanmi.sbc.goods.spec.repository.GoodsInfoSpecDetailRelRepository;
import com.wanmi.sbc.goods.spec.repository.GoodsSpecRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
public class WxLiveAssistantController implements WxLiveAssistantProvider {

    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private WxLiveAssistantService wxLiveAssistantService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsInfoService goodsInfoService;
    @Autowired
    private GoodsInfoSpecDetailRelRepository goodsInfoSpecDetailRelRepository;
    @Autowired
    private GoodsSpecRepository goodsSpecRepository;

    @Override
    public BaseResponse<Long> addAssistant(WxLiveAssistantCreateRequest wxLiveAssistantCreateRequest) {
        return BaseResponse.success(wxLiveAssistantService.addAssistant(wxLiveAssistantCreateRequest));
    }

    @Override
    public BaseResponse<List<String>> deleteAssistant(Long id) {
        List<Goods> goodsList = wxLiveAssistantService.deleteAssistant(id);
        List<String> goodsIds = goodsList.stream().map(Goods::getGoodsId).collect(Collectors.toList());
        return BaseResponse.success(goodsIds);
    }

    @Override
    public BaseResponse<Map<String, String>> updateAssistant(WxLiveAssistantCreateRequest wxLiveAssistantCreateRequest) {
        return BaseResponse.success(wxLiveAssistantService.updateAssistant(wxLiveAssistantCreateRequest));
    }

    @Override
    public BaseResponse<MicroServicePage<WxLiveAssistantVo>> listAssistant(WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest) {
        Page<WxLiveAssistantModel> assistantModelPage = wxLiveAssistantService.listAssistant(wxLiveAssistantSearchRequest);
        List<WxLiveAssistantModel> assistantModels = assistantModelPage.getContent();

        MicroServicePage<WxLiveAssistantVo> microServicePage = new MicroServicePage<>();
        microServicePage.setTotal(assistantModelPage.getTotalElements());
        LocalDateTime now = LocalDateTime.now();
        List<WxLiveAssistantVo> collect = assistantModels.stream().map(assistantModel -> {
            WxLiveAssistantVo wxLiveAssistantVo = new WxLiveAssistantVo();
            LocalDateTime endTime = assistantModel.getEndTime();
            LocalDateTime startTime = assistantModel.getStartTime();
            Duration duration = Duration.between(startTime, endTime);
            wxLiveAssistantVo.setDuration(duration.toMinutes());
            wxLiveAssistantVo.setTheme(assistantModel.getTheme());
            wxLiveAssistantVo.setId(assistantModel.getId());
            wxLiveAssistantVo.setEndTime(endTime.format(df));
            wxLiveAssistantVo.setStartTime(startTime.format(df));
            wxLiveAssistantVo.setGoodsCount(assistantModel.getGoodsCount());
            if(endTime.isBefore(now)){
                wxLiveAssistantVo.setStatus(2);
            }else if(endTime.isAfter(now) && startTime.isBefore(now)){
                wxLiveAssistantVo.setStatus(1);
            }else {
                wxLiveAssistantVo.setStatus(0);
            }
            return wxLiveAssistantVo;
        }).collect(Collectors.toList());
        microServicePage.setContent(collect);
        return BaseResponse.success(microServicePage);
    }

    @Override
    public BaseResponse addGoods(WxLiveAssistantGoodsCreateRequest wxLiveAssistantGoodsCreateRequest) {
        wxLiveAssistantService.addGoods(wxLiveAssistantGoodsCreateRequest);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<List<String>> deleteGoods(Long id) {
        List<Goods> goodsList = wxLiveAssistantService.deleteGoods(id);
        List<String> goodsIds = goodsList.stream().map(Goods::getGoodsId).collect(Collectors.toList());
        return BaseResponse.success(goodsIds);
    }

    @Override
    public BaseResponse<String> updateGoodsInfos(WxLiveAssistantGoodsUpdateRequest wxLiveAssistantGoodsUpdateRequest) {
        return BaseResponse.success(wxLiveAssistantService.updateGoodsInfos(wxLiveAssistantGoodsUpdateRequest));
    }

    @Override
    public BaseResponse<WxLiveAssistantDetailVo> listGoods(WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest) {
        WxLiveAssistantModel assistantModel = wxLiveAssistantService.findAssistantById(wxLiveAssistantSearchRequest.getLiveAssistantId());
        if(assistantModel == null) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划不存在");
        WxLiveAssistantDetailVo wxLiveAssistantDetailVo = new WxLiveAssistantDetailVo();
        wxLiveAssistantDetailVo.setAssistantId(assistantModel.getId());
        wxLiveAssistantDetailVo.setTheme(assistantModel.getTheme());
        wxLiveAssistantDetailVo.setStartTime(assistantModel.getStartTime().format(df));
        wxLiveAssistantDetailVo.setEndTime(assistantModel.getEndTime().format(df));
        LocalDateTime endTime = assistantModel.getEndTime();
        LocalDateTime startTime = assistantModel.getStartTime();
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(startTime, endTime);
        if(endTime.isBefore(now)){
            wxLiveAssistantDetailVo.setStatus(2);
        }else if(endTime.isAfter(now) && startTime.isBefore(now)){
            wxLiveAssistantDetailVo.setStatus(1);
        }else {
            wxLiveAssistantDetailVo.setStatus(0);
        }
        wxLiveAssistantDetailVo.setDuration(duration.toMinutes());
        wxLiveAssistantSearchRequest.setPageSize(100);
        Page<WxLiveAssistantGoodsModel> wxLiveAssistantGoodsPage = wxLiveAssistantService.listGoods(wxLiveAssistantSearchRequest);
        List<WxLiveAssistantGoodsModel> content = wxLiveAssistantGoodsPage.getContent();

        if (CollectionUtils.isNotEmpty(content)) {
            List<String> goodsIds = content.stream().map(WxLiveAssistantGoodsModel::getGoodsId).collect(Collectors.toList());

            List<GoodsSpec> goodsSpecList = goodsSpecRepository.findByGoodsIds(goodsIds);
            Map<Long, List<GoodsSpec>> goodsSpecMap;
            if(CollectionUtils.isNotEmpty(goodsSpecList)){
                goodsSpecMap = goodsSpecList.stream().collect(Collectors.groupingBy(GoodsSpec::getSpecId));
            }else {
                goodsSpecMap = new HashMap<>();
            }
            List<GoodsInfoSpecDetailRel> goodsInfoSpecDetailRelList = goodsInfoSpecDetailRelRepository.findByGoodsIds(goodsIds);
            Map<String, List<GoodsInfoSpecDetailRel>> goodsInfoSpecDetailRelMap;
            if(CollectionUtils.isNotEmpty(goodsSpecList)){
                goodsInfoSpecDetailRelMap = goodsInfoSpecDetailRelList.stream().collect(Collectors.groupingBy(GoodsInfoSpecDetailRel::getGoodsId));
            }else {
                goodsInfoSpecDetailRelMap = new HashMap<>();
            }

            List<Goods> goodsList = goodsService.listByGoodsIds(goodsIds);
            Map<String, List<Goods>> goodsMap = goodsList.stream().collect(Collectors.groupingBy(Goods::getGoodsId));

            List<GoodsInfo> goodsInfoList = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().goodsIds(goodsIds).delFlag(DeleteFlag.NO.toValue()).build());
            Map<String, List<GoodsInfo>> goodsInfoMap = goodsInfoList.stream().collect(Collectors.groupingBy(GoodsInfo::getGoodsId));

            List<WxLiveAssistantGoodsVo> voList = new ArrayList<>();
            for (WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel : content) {
                WxLiveAssistantGoodsVo wxLiveAssistantGoodsVo = new WxLiveAssistantGoodsVo();
                wxLiveAssistantGoodsVo.setAssistantGoodsId(wxLiveAssistantGoodsModel.getId());
                wxLiveAssistantGoodsVo.setGoodsId(wxLiveAssistantGoodsModel.getGoodsId());
                List<Goods> goodsGroup = goodsMap.get(wxLiveAssistantGoodsModel.getGoodsId());
                if (goodsGroup != null) {
                    Goods goods = goodsGroup.get(0);
                    wxLiveAssistantGoodsVo.setAddedFlag(goods.getAddedFlag());
                    wxLiveAssistantGoodsVo.setGoodsNo(goods.getGoodsNo());
                    wxLiveAssistantGoodsVo.setGoodsImg(goods.getGoodsImg());
                    wxLiveAssistantGoodsVo.setGoodsName(goods.getGoodsName());
                    wxLiveAssistantGoodsVo.setMarketPrice(goods.getSkuMinMarketPrice() != null ? goods.getSkuMinMarketPrice().toString() : "0");
                }
                List<GoodsInfo> goodsInfoGroup = goodsInfoMap.get(wxLiveAssistantGoodsModel.getGoodsId());
                //规格
                List<GoodsInfoSpecDetailRel> goodsInfoSpecDetailGroup = goodsInfoSpecDetailRelMap.get(wxLiveAssistantGoodsModel.getGoodsId());

                List<WxLiveAssistantGoodsVo.WxLiveAssistantGoodsInfoVo> wxLiveAssistantGoodsInfoVos = new ArrayList<>();
                Long stockSum = 0L;
                if (goodsInfoGroup != null) {
                    for (GoodsInfo goodsInfo : goodsInfoGroup) {
                        WxLiveAssistantGoodsVo.WxLiveAssistantGoodsInfoVo wxLiveAssistantGoodsInfoVo = new WxLiveAssistantGoodsVo.WxLiveAssistantGoodsInfoVo();
                        wxLiveAssistantGoodsInfoVo.setGoodsInfoName(goodsInfo.getGoodsInfoName());
                        wxLiveAssistantGoodsInfoVo.setGoodsInfoId(goodsInfo.getGoodsInfoId());
                        Long stock = goodsInfo.getStock();
                        if (stock != null){
                            stockSum += stock;
                            wxLiveAssistantGoodsInfoVo.setStock(stock.intValue());
                        }
                        wxLiveAssistantGoodsInfoVo.setGoodsInfoNo(goodsInfo.getGoodsInfoNo());
                        wxLiveAssistantGoodsInfoVo.setMarketPrice(goodsInfo.getMarketPrice() != null ? goodsInfo.getMarketPrice().toString() : "0");

                        if(CollectionUtils.isNotEmpty(goodsInfoSpecDetailGroup)){
                            Map<String, String> goodsInfoSpec = new HashMap<>();
                            for (GoodsInfoSpecDetailRel goodsInfoSpecDetailRel : goodsInfoSpecDetailGroup) {
                                if(!goodsInfo.getGoodsInfoId().equals(goodsInfoSpecDetailRel.getGoodsInfoId())) continue;
                                Long specId = goodsInfoSpecDetailRel.getSpecId();
                                List<GoodsSpec> goodsSpecs = goodsSpecMap.get(specId);
                                if(goodsSpecs != null){
                                    goodsInfoSpec.put(goodsSpecs.get(0).getSpecName(), goodsInfoSpecDetailRel.getDetailName());
                                }
                            }
                            wxLiveAssistantGoodsInfoVo.setGoodsInfoSpec(goodsInfoSpec);
                        }
                        wxLiveAssistantGoodsInfoVos.add(wxLiveAssistantGoodsInfoVo);
                    }
                }
                wxLiveAssistantGoodsVo.setStock(stockSum.intValue());
                wxLiveAssistantGoodsVo.setGoodsInfos(wxLiveAssistantGoodsInfoVos);
                voList.add(wxLiveAssistantGoodsVo);

                wxLiveAssistantDetailVo.setGoods(voList);
            }
        }
        return BaseResponse.success(wxLiveAssistantDetailVo);
    }

    @Override
    public BaseResponse<List<String>> ifGoodsInLive(List<String> goodsIds){
        return BaseResponse.success(wxLiveAssistantService.ifGoodsInLive(goodsIds));
    }

    public BaseResponse<List<String>> afterWxLiveEnd(String message){
        JSONObject wxLiveAssistantMessage = JSONObject.parseObject(message);
        Long assistantId = wxLiveAssistantMessage.getLong("assistantId");
        if(wxLiveAssistantMessage.getInteger("event_type") == 0){
            //开始直播
        }else if(wxLiveAssistantMessage.getInteger("event_type") == 1){
            WxLiveAssistantModel wxLiveAssistantModel = wxLiveAssistantService.findAssistantById(assistantId);
            if(wxLiveAssistantModel != null){
                if(wxLiveAssistantModel.getDelFlag().equals(DeleteFlag.NO) && wxLiveAssistantModel.getEndTime().format(df).equals(wxLiveAssistantMessage.getString("time"))){
                    log.info("直播助手结束直播:{}, 所有直播商品将恢复原价和库存", assistantId);
                    List<Goods> goodsList = wxLiveAssistantService.resetStockAndProce(assistantId);
                    List<String> goodsIds = goodsList.stream().map(Goods::getGoodsId).collect(Collectors.toList());
                    return BaseResponse.success(goodsIds);
                }
            }
        }
        return BaseResponse.SUCCESSFUL();
    }

}