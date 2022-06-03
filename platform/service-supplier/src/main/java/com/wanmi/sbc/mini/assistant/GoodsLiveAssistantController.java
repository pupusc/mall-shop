package com.wanmi.sbc.mini.assistant;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsStockProvider;
import com.wanmi.sbc.elastic.api.request.goods.*;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsResponse;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.api.provider.mini.assistant.WxLiveAssistantProvider;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantGoodsCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantGoodsUpdateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantSearchRequest;
import com.wanmi.sbc.goods.bean.wx.vo.assistant.WxLiveAssistantDetailVo;
import com.wanmi.sbc.goods.bean.wx.vo.assistant.WxLiveAssistantVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/wx/goods")
public class GoodsLiveAssistantController {

    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private WxLiveAssistantProvider wxLiveAssistantProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;
    @Autowired
    private EsGoodsInfoElasticQueryProvider esGoodsInfoElasticQueryProvider;

    /**
     * @description 添加直播计划
     * @param wxLiveAssistantCreateRequest
     * @menu 小程序
     * @status done
     */
    @PostMapping("/assistant/add")
    public BaseResponse addAssistant(@RequestBody WxLiveAssistantCreateRequest wxLiveAssistantCreateRequest){
//        BaseResponse<Long> response = wxLiveAssistantProvider.addAssistant(wxLiveAssistantCreateRequest);
//        if(response.getCode().equals(CommonErrorCode.SUCCESSFUL)){
//            sendDelayMessage(response.getContext(), wxLiveAssistantCreateRequest.getEndTime());
//            return BaseResponse.SUCCESSFUL();
//        }
//        return BaseResponse.FAILED();
        wxLiveAssistantProvider.addAssistant(wxLiveAssistantCreateRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @description 删除直播计划
     * @param id
     * @menu 小程序
     * @status done
     */
    @PostMapping("/assistant/delete")
    public BaseResponse deleteAssistant(@RequestParam Long id){
//        BaseResponse<List<String>> response = wxLiveAssistantProvider.deleteAssistant(id);
//        if(response.getCode().equals(CommonErrorCode.SUCCESSFUL)){
//            List<String> goodsIds = response.getContext();
//            log.info("商品es更新: {}", Arrays.toString(goodsIds.toArray()));
//            if(CollectionUtils.isNotEmpty(goodsIds)){
//                esGoodsInfoElasticProvider.deleteByGoods(EsGoodsDeleteByIdsRequest.builder().deleteIds(goodsIds).build());
//                esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().goodsIds(goodsIds).build());
//            }
////            resetEsStock(response.getContext());
//            return BaseResponse.SUCCESSFUL();
//        }
//        return BaseResponse.FAILED();
        wxLiveAssistantProvider.deleteAssistant(id);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @description 修改直播计划
     * @param wxLiveAssistantCreateRequest
     * @menu 小程序
     * @status done
     */
    @PostMapping("/assistant/update")
    public BaseResponse updateAssistant(@RequestBody WxLiveAssistantCreateRequest wxLiveAssistantCreateRequest){
//        BaseResponse<Map<String, String>> response = wxLiveAssistantProvider.updateAssistant(wxLiveAssistantCreateRequest);
//        if(response.getCode().equals(CommonErrorCode.SUCCESSFUL)){
//            Map<String, String> context = response.getContext();
//            if(context.get("endTime") != null){
//                sendDelayMessage(Long.parseLong(context.get("id")), wxLiveAssistantCreateRequest.getEndTime());
//            }
//            return BaseResponse.SUCCESSFUL();
//        }
//        return BaseResponse.FAILED();
        wxLiveAssistantProvider.updateAssistant(wxLiveAssistantCreateRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @description 查询直播计划
     * @param wxLiveAssistantSearchRequest
     * @menu 小程序
     * @status done
     */
    @PostMapping("/assistant/list")
    public BaseResponse<MicroServicePage<WxLiveAssistantVo>> listAssistant(@RequestBody WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest){
        return wxLiveAssistantProvider.listAssistant(wxLiveAssistantSearchRequest);
    }

    /**
     * @description 添加直播计划商品
     * @param wxLiveAssistantGoodsCreateRequest
     * @menu 小程序
     * @status done
     */
    @PostMapping("/assistant/addGoods")
    public BaseResponse addGoods(@RequestBody WxLiveAssistantGoodsCreateRequest wxLiveAssistantGoodsCreateRequest){
        BaseResponse<List<String>> response = wxLiveAssistantProvider.addGoods(wxLiveAssistantGoodsCreateRequest);
        if(response.getCode().equals(CommonErrorCode.SUCCESSFUL)){
            List<String> goodsIds = response.getContext();
            log.info("商品es更新: {}", Arrays.toString(goodsIds.toArray()));
            if(CollectionUtils.isNotEmpty(goodsIds)){
                esGoodsInfoElasticProvider.deleteByGoods(EsGoodsDeleteByIdsRequest.builder().deleteIds(goodsIds).build());
                esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().goodsIds(goodsIds).build());
            }
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @description 删除直播计划商品
     * @param id 直播计划商品id
     * @menu 小程序
     * @status done
     */
    @PostMapping("/assistant/deleteGoods")
    public BaseResponse deleteGoods(@RequestParam Long id){
        BaseResponse<List<String>> response = wxLiveAssistantProvider.deleteGoods(id);
        if(response.getCode().equals(CommonErrorCode.SUCCESSFUL)){
            List<String> goodsIds = response.getContext();
            log.info("商品es更新: {}", Arrays.toString(goodsIds.toArray()));
            if(CollectionUtils.isNotEmpty(goodsIds)){
                esGoodsInfoElasticProvider.deleteByGoods(EsGoodsDeleteByIdsRequest.builder().deleteIds(goodsIds).build());
                esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().goodsIds(goodsIds).build());
            }
//            resetEsStock(response.getContext());
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @description 更新直播计划商品
     * @param wxLiveAssistantGoodsUpdateRequest
     * @menu 小程序
     * @status done
     */
    @PostMapping("/assistant/updateGoods")
    public BaseResponse updateGoodsInfos(@RequestBody WxLiveAssistantGoodsUpdateRequest wxLiveAssistantGoodsUpdateRequest){
        BaseResponse<String> response = wxLiveAssistantProvider.updateGoodsInfos(wxLiveAssistantGoodsUpdateRequest);
        if(response.getContext() != null){
            log.info("商品es更新: {}", response.getContext());
            esGoodsInfoElasticProvider.deleteByGoods(EsGoodsDeleteByIdsRequest.builder().deleteIds(Collections.singletonList(response.getContext())).build());
            esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().goodsId(response.getContext()).build());
        }else{
            return BaseResponse.FAILED();
        }
        return BaseResponse.SUCCESSFUL();

    }

    /**
     * @description 查询直播计划商品
     * @param wxLiveAssistantSearchRequest
     * @menu 小程序
     * @status done
     */
    @PostMapping("/assistant/listGoods")
    public BaseResponse<WxLiveAssistantDetailVo> listGoods(@RequestBody WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest){
        if(StringUtils.isNotEmpty(wxLiveAssistantSearchRequest.getGoodsName())){
            EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
            queryRequest.setPageNum(0);
            queryRequest.setPageSize(50);
            queryRequest.setLikeGoodsName(wxLiveAssistantSearchRequest.getGoodsName());
            queryRequest.setQueryGoods(true);
            queryRequest.setDelFlag(DeleteFlag.NO.toValue());
            EsGoodsResponse context = esGoodsInfoElasticQueryProvider.pageByGoods(queryRequest).getContext();
            if(context == null || context.getEsGoods() == null || CollectionUtils.isEmpty(context.getEsGoods().getContent())){
                wxLiveAssistantSearchRequest.setGoodsIdIn(Collections.singletonList("nothing"));
            }else{
                List<EsGoodsVO> esGoodsVOS = context.getEsGoods().getContent();
                List<String> goodsIds = esGoodsVOS.stream().map(g -> g.getId()).collect(Collectors.toList());
                wxLiveAssistantSearchRequest.setGoodsIdIn(goodsIds);
            }
        }
        return wxLiveAssistantProvider.listGoods(wxLiveAssistantSearchRequest);
    }

    /**
     * 开启同步
     * @param wxLiveAssistantId
     *
     * @menu 小程序
     * @return
     */
    @GetMapping("/assistant/open-assistant-goods-valid/{wxLiveAssistantId}")
    public BaseResponse open(@PathVariable("wxLiveAssistantId") Long wxLiveAssistantId){
        List<String> goodsIdList = wxLiveAssistantProvider.openAssistantGoodsValid(wxLiveAssistantId).getContext();
        if(CollectionUtils.isNotEmpty(goodsIdList)){
            esGoodsInfoElasticProvider.deleteByGoods(EsGoodsDeleteByIdsRequest.builder().deleteIds(goodsIdList).build());
            esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().goodsIds(goodsIdList).build());
        }
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 关闭同步
     * @param wxLiveAssistantId
     *
     * @menu 小程序
     * @return
     */
    @GetMapping("/assistant/close-assistant-goods-valid/{wxLiveAssistantId}")
    public BaseResponse close(@PathVariable("wxLiveAssistantId") Long wxLiveAssistantId){
        List<String> goodsIdList = wxLiveAssistantProvider.closeAssistantGoodsValid(wxLiveAssistantId).getContext();
        if(CollectionUtils.isNotEmpty(goodsIdList)){
            esGoodsInfoElasticProvider.deleteByGoods(EsGoodsDeleteByIdsRequest.builder().deleteIds(goodsIdList).build());
            esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().goodsIds(goodsIdList).build());
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @description 查询直播计划商品是否在直播计划中
     * @param goodsId
     * @menu 小程序
     * @status done
     */
    @PostMapping("/assistant/ifGoodsInLive")
    public BaseResponse<Boolean> ifGoodsInLive(@RequestParam String goodsId){
        BaseResponse<List<String>> response = wxLiveAssistantProvider.ifGoodsInLive(Collections.singletonList(goodsId));
        if(CollectionUtils.isNotEmpty(response.getContext())){
            if(response.getContext().contains(goodsId)){
                return BaseResponse.success(true);
            }
            return BaseResponse.success(false);
        }
        return BaseResponse.success(false);
    }

//    private void sendDelayMessage(Long assistantId, String endTimeStr){
//        log.info("发送直播助手延时消息: {},{}", assistantId, endTimeStr);
//        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, df);
//        LocalDateTime now2 = LocalDateTime.now();
//        Duration duration2 = Duration.between(now2, endTime);
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("assistantId", assistantId);
//        map.put("time", endTime.format(df));
//        map.put("event_type", 1);
//        wxMiniMessageProducer.sendDelay(map, duration2.toMillis());
//    }

//    public void resetEsStock(Map<String, Map<String, Integer>> map){
//
//        if(map != null && !map.isEmpty()){
//            Map<String, Integer> skusMap = map.get("skus");
//            if(!skusMap.isEmpty()){
//                EsGoodsSkuStockSubRequest esGoodsSkuStockSubRequest = EsGoodsSkuStockSubRequest.builder().skusMap(skusMap).build();
//                esGoodsStockProvider.batchResetStockBySkuId(esGoodsSkuStockSubRequest);
//            }
//            Map<String, Integer> spusMap = map.get("spus");
//            if(!spusMap.isEmpty()){
//                EsGoodsSpuStockSubRequest esGoodsSpuStockSubRequest = EsGoodsSpuStockSubRequest.builder().spusMap(spusMap).build();
//                esGoodsStockProvider.batchResetStockBySpuId(esGoodsSpuStockSubRequest);
//            }
//        }
//    }
}
